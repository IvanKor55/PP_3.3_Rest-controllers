let allUsers = null;
let allRoles = null;
// Формирование панели Admin
async function getAllUsers(mode) {
    if (mode) {
        // Получение данных админа, списка всех ролей и первоначального списка user-ов
        await fetch('/admin/users', {method: 'GET'})
            .then(response => response.json())
            .then(adminPanel => {
                $('#userLogin').append(`<span><strong>${adminPanel.user.firstName}&nbsp;</strong></span>`);
                $('#userRoles').append('<span>&nbsp;'
                    + adminPanel.user.roles.map(role => role.authority.replace('ROLE_', ' '))
                    + '</span>');
                allRoles = adminPanel.rolesList;
                allUsers = adminPanel.users;
            })
            .catch(error => {
                console.error('Ошибка при получении данных:', error)
            })
    }
    // Формирование данных user-ов с кнопками редактирования и удаления
    let allUsersRow = '';
    allUsers.map(user => {
        allUsersRow +=
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
                    data-bs-target="#user-modal"
                    onclick="userModal(${user.id}, false)">
                    Edit
                </button>
            </td>
        <!-- Кнопка Delete-->
            <td>
                <button type="button" class="btn btn-danger delete-button" id="delete-button"
                    data-bs-target="#user-modal"
                    onclick="userModal(${user.id}, true)">
                    Delete
                </button>
            </td>
       </tr>`
    })
    $('#users').empty().append(allUsersRow)
}

let modal = null;
// Формирование модального окна
function userModal(userId, modeDelete) {
    // кнопка Close
    let button = `<button type="button" class="btn btn-secondary" 
                        data-bs-dismiss="modal" aria-label="Close">Close</button>`;
    let rolesSelect = "";

    if (!userId) {
        // кнопка добавления user-а
        $('#user-modal').find('.modal-title').text('Add new user');
        button += '<button type="submit" class="btn btn-success" onclick="editUser(false)">Add new user</button>';
        allRoles.map(role => {
            rolesSelect += '<option value=' + role.id
                + '>' + role.authority.replace('ROLE_', '') + '</option>'
        })
        // очистка формы ввода нового user-а
        $('#user-modal').find('#userid').val('');
        $('#user-modal').find('#firstname').val('');
        $('#user-modal').find('#lastname').val('');
        $('#user-modal').find('#age').val('');
        $('#user-modal').find('#username').val('');
        $('#user-modal').find('#password').val('');
        const errorContainer = document.getElementById('error-messages');
        errorContainer.innerHTML = '';
        errorContainer.style.display = 'none';
    } else {
        // поиск по списку user-а для отбражения его данных
        for (const user of allUsers) {
            if (user.id === userId) {
                // отображение данных user-а
                $('#user-modal').find('#userid').val(user.id);
                $('#user-modal').find('#firstname').val(user.firstName);
                $('#user-modal').find('#lastname').val(user.lastName);
                $('#user-modal').find('#age').val(user.age);
                $('#user-modal').find('#username').val(user.login);
                $('#user-modal').find('#password').val(user.password);
                const errorContainer = document.getElementById('error-messages');
                errorContainer.innerHTML = '';
                errorContainer.style.display = 'none';
                // формирование кнопки удаления user-а
                if (modeDelete) {
                    $('#user-modal').find('.modal-title').text('Delete user');
                    button += '<button type="submit" class="btn btn-danger" onclick="deleteUser('
                        + user.id + ')">Delete</button>';
                    // вывод ролей user-а
                    user.roles.map(role => {
                        rolesSelect += '<option value=' + role.id
                            + '>' + role.authority.replace('ROLE_', '') + '</option>'
                    })
                } else {
                    // формирование кнопки редактирования user-а
                    $('#user-modal').find('.modal-title').text('Edit user');
                    button += '<button type="submit" class="btn btn-success" onclick="editUser('
                        + user.id + ')">Update</button>';
                    // вывод всех ролей
                    allRoles.map(role => {
                        rolesSelect += '<option value=' + role.id
                            + '>' + role.authority.replace('ROLE_', '') + '</option>'
                    })
                }
                break;
            }
        }
    }
    $('#user-modal').find('#roleSelect').empty().append(rolesSelect);
    $('#user-modal').find('.modal-footer').empty().append(button);

    const modalElement = document.getElementById('user-modal');
    modal = new bootstrap.Modal(modalElement);
    modal.show();

}

// Удаление user-а
async function deleteUser(id) {
    await fetch(`/admin/delete?id=` + id, {method: 'POST'})
        .then(response => {
            if (response.ok) {
                let i = 0;
                for (const user of allUsers) {
                    if (user.id === id) {
                        console.log(user);
                        allUsers.splice(i, 1);
                        break;
                    }
                    i++;
                }
                modal.hide();
                getAllUsers(false)
            }
        })
}

// Обработка данных из модального окна для редактирования user-а или сохранения нового
async function editUser(id) {
    // формирование массива сущностей role
    let rolesSelected = $('#user-modal').find('#roleSelect').val().map(roleId => {
        return {
            'id': parseInt(roleId),
            'authority': allRoles.find(role => role.id == roleId).authority
        }
    });
    // формирование сущности user
    let userUpdate = {
        'id': parseInt($('#user-modal').find('#userid').val()),
        'firstName': $('#user-modal').find('#firstname').val(),
        'lastName': $('#user-modal').find('#lastname').val(),
        'age': $('#user-modal').find('#age').val(),
        'login': $('#user-modal').find('#username').val(),
        'password': $('#user-modal').find('#password').val(),
        'roles': rolesSelected
    };
    // отправка данных в контроллер
    await fetch('/admin/save', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userUpdate)
    })
        .then(async response => {
            if (response.ok) {
                if (!id) {
                    // получение обратно нового user-а c присвоенным id
                    await fetch('/admin/new', {method: 'GET'})
                        .then(response => response.json())
                        .then(user => {
                            let userNew = user;
                            // добавление нового user-а в список всех user-ов
                            allUsers.push(userNew);
                        });
                } else {
                    let i = 0;
                    // поиск редактируемого user-а в списке всех user-ов
                    for (const user of allUsers) {
                        if (user.id === id) {
                            // замещение данных редактируемого user-а
                            allUsers.splice(i, 1, userUpdate);
                            break;
                        }
                        i++;
                    }
                }
                modal.hide();
                getAllUsers(false);
            } else {
                // вывод оошибок валидации
                const errorContainer = document.getElementById('error-messages');
                errorContainer.innerHTML = '';
                let errors = (await response.text()).replace(/[\"\]\[]/g,'').split(',');
                errors.map(errorMessage => {
                    const errorElement = document.createElement('div');
                    console.log('errorMessage = ', errorMessage);
                    errorElement.textContent = errorMessage;
                    errorContainer.appendChild(errorElement);
                });
                errorContainer.style.display = 'block'; // Показать сообщение об ошибке
            }
            if (!id) {
                document.getElementById('user-tab').click();
            }
        });
}

getAllUsers(true);