console.log("Modal js");

const deleteUrl="http://localhost:8081/user/contacts/delete/";

const viewContact = document.getElementById("view_contact_modal");
// option
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};
// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

const contactModal = new Modal(viewContact, options, instanceOptions);

function openContactModal() {
  contactModal.show();
}
function closeContactModal() {
  contactModal.hide();
}

// async function deletedContactById(id) {
//   console.log("Delete user with id " + id);
//   const response = await fetch(
//     `http://localhost:8081/api/deleteContacts/${id}`,
//     {
//       method: "DELETE",
//     }
//   );
//   const data = await response.json();
//   console.log(data);
// }

async function loadContactData(id) {
  console.log("Load data function " + id);

  const response = await fetch(`http://localhost:8081/api/contacts/${id}`);
  const data = await response.json();
  console.log(data);
  document.querySelector("#contact_name").innerHTML = data.name;
  document.querySelector("#contact_email").innerHTML = data.email;
  document.querySelector("#contact_image").src = data.profilePic;
  document.querySelector("#contact_about").innerHTML = data.description;
  document.querySelector("#contact_address").innerHTML = data.address;
  document.querySelector("#contact_linkedin").innerHTML = data.linkedinLink;

  openContactModal();

  return data;

  //   fetch(`http://localhost:8081/api/contacts/${id}`)
  //     .then(async (response) => {
  //       let data = await response.json();
  //       console.log(data);
  //       return data;
  //     })
  //     .catch((error) => {
  //       console.log(error);
  //     });
}
async function deleteContact(id) {
  Swal.fire({
    title: "Do you want to Delete the contact?",
    showCancelButton: true,
    icon:"warning",
    confirmButtonColor:"red",
    cancelButtonColor:"blue",

    confirmButtonText: "Save",
    
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      const u=deleteUrl+id;
      window.location.replace(u);
      console.log(id+" ");
      Swal.fire("Deleted!", "", "success");
    } else if (result.isDenied) {
      Swal.fire("Changes are not saved", "", "info");
    }
  });
}
