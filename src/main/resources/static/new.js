console.log("Start new")
function addNewUser() {
    fetch('/admin/users', {method: 'GET'})
        .then(response => response.json())
        .then(adminPanel => {
            let rolesSelect = ""
            adminPanel.rolesList.map(role => {
                rolesSelect += '<option value=' + role.id
                    + '>' + role.authority.replace('ROLE_', '') + '</option>'
            })
            console.log(rolesSelect)
            $('#rolesList').append(rolesSelect)
        })
}
addNewUser();