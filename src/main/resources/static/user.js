function getUser(userId) {
    fetch('/user/user?id=' + userId, {method: 'GET'})
        .then(function (response) {
            if (response.status === 404 || response.status === 400) {
                response.text().then((value) => console.warn("Ошибка загрузки пользователя: " + value));
                return;
            } else {
                response.json().then(user => {
                    $('#userLogin').append('<span><strong>'
                        + user.firstName + '&nbsp;</strong></span>')
                    $('#userRoles').append('<span>&nbsp;'
                        + user.roles.map(role => role.authority.replace('ROLE_', ' '))
                        + '</span>')
                    $('#userDetail').append('<tr>'
                        + '<td>' + user.id + '</td>'
                        + '<td>' + user.firstName + '</td>'
                        + '<td>' + user.lastName + '</td>'
                        + '<td>' + user.age + '</td>'
                        + '<td>' + user.login + '</td>'
                        + '<td>' + user.roles.map(role => role.authority.replace('ROLE_', ' ')) + '</td>'
                        + '</tr>')
                })
            }
        })
}
