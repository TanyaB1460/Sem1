<#assign title="Вход в систему">
<#include "base.ftlh">

<@base>
    <div class="card" style="max-width: 400px; margin: 0 auto;">
        <h2>Вход в аккаунт</h2>

        <form action="/login" method="post" onsubmit="return validateLoginForm()">
            <div class="form-group">
                <label for="username">Имя пользователя:</label>
                <input type="text" id="username" name="username" required>
            </div>

            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%;">Войти</button>
        </form>

        <p style="text-align: center; margin-top: 1rem;">
            Нет аккаунта? <a href="/register">Зарегистрируйтесь</a>
        </p>
    </div>

    <script>
        function validateLoginForm() {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;

            if (username.length < 3) {
                alert('Имя пользователя должно содержать минимум 3 символа');
                return false;
            }

            if (password.length < 6) {
                alert('Пароль должен содержать минимум 6 символов');
                return false;
            }

            return true;
        }
    </script>
</@base>