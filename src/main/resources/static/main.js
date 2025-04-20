const URL = "/api/users";

const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

$(document).ready(function () {
    getUsers();
})

function loadUserHeader() {
    fetch("/api/users/me", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(res => res.json())
        .then(user => {
            const username = user.username;
            const roles = user.roles.map(role => role.name.replace("ROLE_", "")).join(", ");
            document.getElementById("user-info-header").innerHTML =
                `<span class="fw-bold">${username}</span> with roles: ${roles}`;
        });
}

document.addEventListener("DOMContentLoaded", loadUserHeader);


function getUsers() {
    fetch(URL)
        .then(function (response) {
            return response.json();
        })
        .then(function (users) {
            let placeholder = document.getElementById('data_output');
            let out = "";
            for (let user of users) {
                out += '<tr>';
                out += '<td>' + user.id + '</td>';
                out += '<td>' + user.firstName + '</td>';
                out += '<td>' + user.lastName + '</td>';
                out += '<td>' + user.age + '</td>';
                out += '<td>' + user.email + '</td>';

                // Обработка ролей
                let roles = user.roles.map(role => role.name.replace("ROLE_", "")).join(", ");
                out += '<td>' + roles + '</td>';

                out += '<td>' +
                    '<button type="button" class="btn btn-info" data-bs-target="#editModal" data-bs-toggle="modal" ' +
                    'onclick="getEditModal(' + user.id + ')">' + 'Edit' +
                    '</button>' +
                    '</td>';
                out += '<td>' +
                    '<button type="button" class="btn btn-danger" data-bs-target="#deleteModal" data-bs-toggle="modal" ' +
                    'onclick="getDeleteModal(' + user.id + ')">' + 'Delete' +
                    '</button>' +
                    '</td>';
                out += '</tr>';
            }


            placeholder.innerHTML = out;
        });
}

function loadUserInfo() {
    fetch("/api/users/me", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(res => res.json())
        .then(user => {
            document.getElementById("info_id").textContent = user.id;
            document.getElementById("info_firstname").textContent = user.firstName;
            document.getElementById("info_lastname").textContent = user.lastName;
            document.getElementById("info_age").textContent = user.age;
            document.getElementById("info_email").textContent = user.email;
            document.getElementById("info_roles").textContent = user.roles
                .map(role => role.name.replace("ROLE_", ""))
                .join(", ");
        });
}



function getEditModal(id) {
    fetch(URL + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => res.json())
        .then(userEdit => {
            document.getElementById('edit_id').value = userEdit.id;
            document.getElementById('edit_firstName').value = userEdit.firstName;
            document.getElementById('edit_lastName').value = userEdit.lastName;
            document.getElementById('edit_age').value = userEdit.age;
            document.getElementById('edit_email').value = userEdit.email;

            const select = document.getElementById('edit_role');
            const userRoles = userEdit.roles.map(role => role.name);

            for (let option of select.options) {
                option.selected = userRoles.includes(option.value);
            }
        });
}


function editUser() {
    event.preventDefault();
    let id = document.getElementById('edit_id').value;
    let firstName = document.getElementById('edit_firstName').value;
    let lastName = document.getElementById('edit_lastName').value;
    let age = document.getElementById('edit_age').value;
    let email = document.getElementById('edit_email').value;
    let password = document.getElementById('edit_password').value;
    let roles = $("#edit_role").val()

    if (!firstName) {
        alert("First name is required!");
        return;
    }
    if (!lastName) {
        alert("Last name is required!");
        return;
    }
    if (!age) {
        alert("Age is required!");
        return;
    }
    if (!email) {
        alert("Email is required!");
        return;
    }
    if (!password) {
        alert("Password is required!");
        return;
    }
    if (!roles || roles.length === 0) {
        alert("At least one role must be selected!");
        return;
    }

    for (let i = 0; i < roles.length; i++) {
        if (roles[i] === 'ROLE_ADMIN') {
            roles[i] = {
                'id': 2,
                'role': 'ROLE_ADMIN',
                "authority": "ROLE_ADMIN"
            }
        }
        if (roles[i] === 'ROLE_USER') {
            roles[i] = {
                'id': 1,
                'role': 'ROLE_USER',
                "authority": "ROLE_USER"
            }
        }
    }

    fetch(URL, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
            'id': id,
            'firstName': firstName,
            'lastName': lastName,
            'age': age,
            'email': email,
            'password': password,
            'roles': roles
        })
    })
        .then(() => {
            $('#editModal').modal('hide');
            getUsers();
        })
}

function getDeleteModal(id) {
    fetch(URL + '/' + id, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8',
            [csrfHeader]: csrfToken
        }
    }).then(res => {
        res.json().then(userDelete => {
            document.getElementById('delete_id').value = userDelete.id;
            document.getElementById('delete_firstName').value = userDelete.firstName;
            document.getElementById('delete_lastName').value = userDelete.lastName;
            document.getElementById('delete_age').value = userDelete.age;
            document.getElementById('delete_email').value = userDelete.email;
            document.getElementById('delete_role').value = userDelete.roles;

            const select = document.getElementById('delete_role');
            const userRoles = userDelete.roles.map(role => role.name);
            for (let option of select.options) {
                option.selected = userRoles.includes(option.value);
            }
        })
    });
}

function deleteUser() {
    event.preventDefault();
    let id = document.getElementById('delete_id').value;

    fetch(URL + '/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            [csrfHeader]: csrfToken
        },

    })
        .then(() => {
            $('#deleteModal').modal('hide');
            getUsers();
        })
}

function addUser() {
    event.preventDefault();
    let firstName = document.getElementById('create_firstName').value;
    let lastName = document.getElementById('create_lastName').value;
    let age = document.getElementById('create_age').value;
    let email = document.getElementById('create_email').value;
    let password = document.getElementById('create_password').value;
    let roles = $("#create_role").val();

    let roleObjects = roles.map(role => {
        if (role === 'ROLE_ADMIN') {
            return { id: 2, role: 'ROLE_ADMIN', authority: 'ROLE_ADMIN' };
        } else {
            return { id: 1, role: 'ROLE_USER', authority: 'ROLE_USER' };
        }
    });

    fetch(URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
            firstName,
            lastName,
            age,
            email,
            password,
            roles: roleObjects
        })
    })
        .then(() => {
            document.getElementById('nav-users_table-tab').click();
            getUsers();
            document.newUserForm.reset();
        });
}


window.addEventListener("DOMContentLoaded", () => {
    document.getElementById("v-pills-user-tab").addEventListener("click", loadUserInfo);
});



/*  Методы вставки:
         const textElement = document.getElementById('admin_table');
         const newElement = document.createElement('div');
         newElement.innerHTML = `Some text <span class="yellow">yellow text</span>another text!`;

         Вставка:
            1. Перед обектом -> textElement.before(newElement);
            2. После -> after
            3. Внутрь и в начало объекта -> prepend
            4. Внутрь в конец -> append

         Альтернатива вставки текста, HTML, элемента:
               textElement.insertAdjacentHTML(
                   'afterend',
                   `<p>Some text</p>`
               );
               (beforebegin, afterbegin, beforeend, afterend)



        Работа с link и input:

            const link = document.querySelector('.lesson_link');
            const input = document.querySelector('.lesson_input');

            console.log(link.href);
            console.log(input.value);
            */


/* jQuery
* $(document).ready(function() {
*       пишем на jQuery
* })
*
* Получить эл-т на странице
*  $('#admin_table') - получим селектор по id
*
* AJAX запросы
*   $.ajax({
*       type: "method",'
*       url: "url",
*       data: "data",
*       dataType: "dataType",
*       success: function (response) {
*           //Обрабатываем ответ
*       }
*   });
* */
