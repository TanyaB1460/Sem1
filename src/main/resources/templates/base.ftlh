<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!"LangExchange"} - Обмен языками</title>
    <style>
        /* ваш CSS */
    </style>
</head>
<body>
<header>
    <div class="container">
        <nav class="navbar">
            <a href="/" class="logo">🌍 LangExchange</a>
            <ul class="nav-links">
                <li><a href="/">Главная</a></li>
                <li><a href="/search">Поиск</a></li>
            </ul>
            <div class="auth-buttons">
                <#if user??>
                    <span>Привет, ${user.username}!</span>
                    <a href="/profile" class="btn btn-outline">Профиль</a>
                    <a href="/logout" class="btn btn-primary">Выйти</a>
                <#else>
                    <a href="/login" class="btn btn-outline">Вход</a>
                    <a href="/register" class="btn btn-primary">Регистрация</a>
                </#if>
            </div>
        </nav>
    </div>
</header>

<#macro base>
    <main>
        <div class="container">
            <#if error??>
                <div class="alert alert-error">${error}</div>
            </#if>
            <#if message??>
                <div class="alert alert-success">${message}</div>
            </#if>

            <#nested>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2024 LangExchange. Общайтесь, учитесь, развивайтесь!</p>
        </div>
    </footer>
</#macro>

</body>
</html>