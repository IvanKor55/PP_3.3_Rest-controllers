console.log("admin")
async function getAllUsers() {
    fetch('/admin/users', {method: 'GET'})
        .then(response => response.json())
        .then(adminPanel => {
            $('#userLogin').append(`<span><strong>${adminPanel.user.firstName}&nbsp;</strong></span>`)
            $('#userRoles').append('<span>&nbsp;'
                + adminPanel.user.roles.map(role => role.authority.replace('ROLE_', ' '))
                + '</span>')
            let allusers = '';
            adminPanel.users.map(user => {
                allusers +=
                    `<tr>
                        <td>${user.id}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.login}</td>`
                    + '<td>' + user.roles.map(role => role.authority.replace('ROLE_', ' ')) + '</td>\n'
                    + `<!-- Кнопка Edit -->
                         <td>
                            <button type="button" class="btn btn-primary" id="edit-button"
                                data-user-id=${user.id}
                                data-bs-target="#editModal${user.id}"
                                data-bs-toggle="modal">
                                Edit
                            </button>
                            <div th:replace="~{edit :: content}"></div>
                        </td>
                    <!-- Кнопка Delete-->
<!--                    onclick="bootstrap.Modal.getOrCreateInstance(document.getElementById('#deleteModal${user.id}')).show()">-->
                        <td>
                            <button type="button" class="btn btn-danger delete-button" id="delete-button"
                                data-user-id=${user.id} 
                                data-bs-target="#deleteModal${user.id}"
                                onclick=openModal('#deleteModal${user.id}')>
                                Delete
                            </button>
                            <div th:replace="~{delete :: content}"></div>
                        </td>
                   </tr>`
            })
            // console.log(allusers)
            $('#users').append(allusers)
        })
        .catch(error => {
            console.error('Ошибка данных:', error)
        })
}
let modal = null;
function getModal(id) {
    if (!modal) {
        modal = new bootstrap.Modal(id);
    }
    return modal
}

async function toggleModal(id) {
    // modal.toggle();
    await getModal(id).show()
    modal = null;
}

// const openModal = (name) => {
//     return new Promise((resolve, reject) => {
//             let modal = null
//             if (!modal) {
//                 let modalElement = document.getElementById(name)
//                 modal = new bootstrap.Modal(modalElement)
//             }
//         resolve(modal.show())
//     })
// }
const openModal = (name) => {
    let modalElement = document.getElementById(name)
    modal = new bootstrap.Modal(modalElement)
    modal.show()
}
getAllUsers()