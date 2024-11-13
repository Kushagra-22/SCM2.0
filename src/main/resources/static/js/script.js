console.log("Script loaded...");

let currentTheme = getTheme();
changeTheme();
function changeTheme() {
  // set theme at web page
  const changeThemeButton = document.querySelector("#thm_chng_btn");

  document.querySelector("html").classList.add(currentTheme);
  console.log(currentTheme);
  document.getElementById("thm_chng_btn").addEventListener("click", () => {
    let oldTheme = currentTheme;
    if (currentTheme == "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }
    setTheme(currentTheme);
    document.querySelector("html").classList.remove(oldTheme);

    document.querySelector("html").classList.add(currentTheme);

    //change name of theme button
    changeThemeButton.querySelector("span").textContent =
      currentTheme == "light" ? "Dark" : "Light";
  });
}
// Set theme to local storage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// get theme from local storage
function getTheme() {
  let theme = localStorage.getItem("theme");
  if (theme) return theme;
  else {
    return "light";
  }
}
