<#assign title="Регистрация">
<#include "base.ftlh">

<@base>
    <div class="card" style="max-width: 500px; margin: 0 auto;">
        <h2>Создание аккаунта</h2>

        <form action="/register" method="post" onsubmit="return validateRegisterForm()">
            <div class="form-group">
                <label for="username">Имя пользователя *</label>
                <input type="text" id="username" name="username" required minlength="3">
                <small>Минимум 3 символа</small>
            </div>

            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="password">Пароль *</label>
                <input type="password" id="password" name="password" required minlength="6">
                <small>Минимум 6 символов</small>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="nativeLanguage">Родной язык *</label>
                    <select id="nativeLanguage" name="nativeLanguage" required>
                        <option value="">Выберите язык</option>
                        <option value="russian">Русский</option>
                        <option value="english">Английский</option>
                        <option value="spanish">Испанский</option>
                        <option value="french">Французский</option>
                        <option value="german">Немецкий</option>
                        <option value="chinese">Китайский</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="learningLanguage">Изучаемый язык *</label>
                    <select id="learningLanguage" name="learningLanguage" required>
                        <option value="">Выберите язык</option>
                        <option value="english">Английский</option>
                        <option value="russian">Русский</option>
                        <option value="spanish">Испанский</option>
                        <option value="french">Французский</option>
                        <option value="german">Немецкий</option>
                        <option value="chinese">Китайский</option>
                        <option value="japanese">Японский</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="level">Уровень владения</label>
                <select id="level" name="level">
                    <option value="beginner">Начинающий (A1-A2)</option>
                    <option value="intermediate" selected>Средний (B1-B2)</option>
                    <option value="advanced">Продвинутый (C1-C2)</option>
                    <option value="native">Носитель</option>
                </select>
            </div>

            <div class="form-group">
                <label>Интересы (выберите один или несколько):</label>
                <div class="checkbox-group">
                    <#if interests??>
                        <#list interests as interest>
                            <div class="checkbox-item">
                                <input type="checkbox" id="interest-${interest.id}" name="interests" value="${interest.id}">
                                <label for="interest-${interest.id}">${interest.name}</label>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%;">Зарегистрироваться</button>
        </form>

        <p style="text-align: center; margin-top: 1rem;">
            Уже есть аккаунт? <a href="/login">Войдите</a>
        </p>
    </div>

    <script>
        function validateRegisterForm() {
            const username = document.getElementById('username').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const nativeLanguage = document.getElementById('nativeLanguage').value;
            const learningLanguage = document.getElementById('learningLanguage').value;

            // Валидация имени пользователя
            if (username.length < 3) {
                alert('Имя пользователя должно содержать минимум 3 символа');
                return false;
            }

            // Валидация email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert('Введите корректный email адрес');
                return false;
            }

            // Валидация пароля
            if (password.length < 6) {
                alert('Пароль должен содержать минимум 6 символов');
                return false;
            }

            // Проверка выбора языков
            if (nativeLanguage === learningLanguage) {
                alert('Родной и изучаемый язык не могут совпадать');
                return false;
            }

            return true;
        }

        // Динамическая проверка совпадения языков
        document.getElementById('nativeLanguage').addEventListener('change', checkLanguages);
        document.getElementById('learningLanguage').addEventListener('change', checkLanguages);

        function checkLanguages() {
            const native = document.getElementById('nativeLanguage').value;
            const learning = document.getElementById('learningLanguage').value;

            if (native && learning && native === learning) {
                alert('Родной и изучаемый язык не могут совпадать!');
                document.getElementById('learningLanguage').value = '';
            }
        }
    </script>

    <style>
        .checkbox-group {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 0.5rem;
            margin-top: 0.5rem;
        }

        .checkbox-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        small {
            color: #666;
            font-size: 0.8rem;
        }
    </style>
</@base>