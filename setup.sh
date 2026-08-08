#!/bin/bash
# =============================================
# SCM2.0 Infrastructure Setup Script
# =============================================
# This script sets up the complete infrastructure:
#   1. Kind cluster
#   2. NGINX Ingress Controller
#   3. ArgoCD
#   4. Docker image build + load
#   5. K8s manifests deployment
#
# Prerequisites:
#   - Docker running
#   - kind installed
#   - kubectl installed
#   - (Optional) argocd CLI installed
# =============================================

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

CLUSTER_NAME="scm2-cluster"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

log() { echo -e "${GREEN}[✓]${NC} $1"; }
warn() { echo -e "${YELLOW}[!]${NC} $1"; }
error() { echo -e "${RED}[✗]${NC} $1"; exit 1; }
info() { echo -e "${BLUE}[i]${NC} $1"; }

header() {
    echo ""
    echo -e "${BLUE}=============================================${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}=============================================${NC}"
    echo ""
}

# =============================================
# Step 0: Prerequisite checks
# =============================================
header "Checking Prerequisites"

command -v docker >/dev/null 2>&1 || error "Docker is not installed"
command -v kind >/dev/null 2>&1 || error "Kind is not installed"
command -v kubectl >/dev/null 2>&1 || error "kubectl is not installed"

docker info >/dev/null 2>&1 || error "Docker daemon is not running"

log "All prerequisites met"

# =============================================
# Step 1: Create Kind Cluster
# =============================================
header "Creating Kind Cluster"

if kind get clusters 2>/dev/null | grep -q "$CLUSTER_NAME"; then
    warn "Cluster '$CLUSTER_NAME' already exists"
    read -p "Delete and recreate? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        kind delete cluster --name "$CLUSTER_NAME"
        log "Old cluster deleted"
    else
        info "Using existing cluster"
    fi
fi

if ! kind get clusters 2>/dev/null | grep -q "$CLUSTER_NAME"; then
    kind create cluster \
        --name "$CLUSTER_NAME" \
        --config "$PROJECT_DIR/kind-config.yaml" \
        --wait 60s
    log "Kind cluster '$CLUSTER_NAME' created"
fi

kubectl cluster-info --context "kind-$CLUSTER_NAME"
log "Cluster is ready"

# =============================================
# Step 2: Build and Load Docker Image
# =============================================
header "Building Docker Image"

# Build the app JAR first (if target doesn't exist)
if [ ! -f "$PROJECT_DIR/target/scm2.0-0.0.1-SNAPSHOT.jar" ]; then
    info "Building Maven project..."
    if command -v mvn >/dev/null 2>&1; then
        cd "$PROJECT_DIR" && mvn clean package -DskipTests -B
    elif [ -f "$PROJECT_DIR/mvnw" ]; then
        cd "$PROJECT_DIR" && ./mvnw clean package -DskipTests -B
    else
        error "Maven not found. Build the JAR first: mvn clean package -DskipTests"
    fi
fi

# Build Docker image
docker build -t scm2.0:latest "$PROJECT_DIR"
log "Docker image built: scm2.0:latest"

# Load into Kind cluster
kind load docker-image scm2.0:latest --name "$CLUSTER_NAME"
log "Image loaded into Kind cluster"

# =============================================
# Step 3: Install NGINX Ingress Controller
# =============================================
header "Installing NGINX Ingress Controller"

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

info "Waiting for Ingress Controller to be ready..."
kubectl wait --namespace ingress-nginx \
    --for=condition=ready pod \
    --selector=app.kubernetes.io/component=controller \
    --timeout=120s 2>/dev/null || warn "Ingress controller may still be starting"

log "NGINX Ingress Controller installed"

# =============================================
# Step 4: Install ArgoCD
# =============================================
header "Installing ArgoCD"

kubectl create namespace argocd 2>/dev/null || true
kubectl apply -n argocd \
    -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

info "Waiting for ArgoCD to be ready..."
kubectl wait --namespace argocd \
    --for=condition=ready pod \
    --selector=app.kubernetes.io/name=argocd-server \
    --timeout=180s 2>/dev/null || warn "ArgoCD may still be starting"

log "ArgoCD installed"

# =============================================
# Step 5: Deploy K8s Manifests
# =============================================
header "Deploying Application Stack"

info "Applying MySQL resources..."
kubectl apply -f "$PROJECT_DIR/k8s/mysql/mysql-secret.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/mysql/mysql-pvc.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/mysql/mysql-deployment.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/mysql/mysql-service.yaml"
log "MySQL resources applied"

info "Waiting for MySQL to be ready..."
kubectl wait --for=condition=ready pod \
    --selector=app=mysql \
    --timeout=120s 2>/dev/null || warn "MySQL may still be starting"

info "Applying App resources..."
kubectl apply -f "$PROJECT_DIR/k8s/app/app-deployment.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/app/app-service.yaml"
kubectl apply -f "$PROJECT_DIR/k8s/app/app-ingress.yaml"
log "App resources applied"

info "Waiting for App pods to be ready..."
kubectl wait --for=condition=ready pod \
    --selector=app=scm2 \
    --timeout=180s 2>/dev/null || warn "App pods may still be starting"

# =============================================
# Step 6: Apply ArgoCD Applications
# =============================================
header "Configuring ArgoCD Applications"

kubectl apply -f "$PROJECT_DIR/k8s/argocd-app.yaml"
log "ArgoCD applications created"

# =============================================
# Step 7: Print Summary
# =============================================
header "Setup Complete! 🎉"

echo ""
echo -e "${GREEN}======== Access Information ========${NC}"
echo ""

# App URL
echo -e "  ${BLUE}Application URL:${NC}"
echo -e "    http://localhost:30007"
echo ""

# ArgoCD
ARGOCD_PASSWORD=$(kubectl -n argocd get secret argocd-initial-admin-secret \
    -o jsonpath="{.data.password}" 2>/dev/null | base64 --decode 2>/dev/null || echo "still-initializing")
echo -e "  ${BLUE}ArgoCD UI:${NC}"
echo -e "    Run: kubectl port-forward svc/argocd-server -n argocd 8080:443"
echo -e "    URL: https://localhost:8080"
echo -e "    Username: admin"
echo -e "    Password: ${ARGOCD_PASSWORD}"
echo ""

# Ingress
echo -e "  ${BLUE}Ingress (optional):${NC}"
echo -e "    Add to /etc/hosts: 127.0.0.1 scm2.local"
echo -e "    URL: http://scm2.local"
echo ""

echo -e "${GREEN}======== Pod Status ========${NC}"
echo ""
kubectl get pods -A --field-selector=status.phase!=Succeeded 2>/dev/null || kubectl get pods -A
echo ""

echo -e "${GREEN}======== Services ========${NC}"
echo ""
kubectl get svc
echo ""

echo -e "${YELLOW}======== Failure Simulation ========${NC}"
echo ""
echo -e "  To trigger the intentional failure:"
echo -e "    ${RED}kubectl apply -f k8s/failure-sim/app-deployment-broken.yaml${NC}"
echo ""
echo -e "  To fix it:"
echo -e "    ${GREEN}kubectl apply -f k8s/app/app-deployment.yaml${NC}"
echo ""

echo -e "${GREEN}======== Useful Commands ========${NC}"
echo ""
echo -e "  kubectl get pods                         # Check pod status"
echo -e "  kubectl logs -f <pod-name>               # Stream pod logs"
echo -e "  kubectl describe pod <pod-name>           # Pod details & events"
echo -e "  kubectl get events --sort-by=.lastTimestamp  # Cluster events"
echo -e "  kubectl top pods                          # Resource usage"
echo -e "  kind delete cluster --name $CLUSTER_NAME  # Tear down"
echo ""
