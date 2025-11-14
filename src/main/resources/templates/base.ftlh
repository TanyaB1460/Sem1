<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!"LangExchange"} - Обмен языками</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background: #f4f4f4; }
        .container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
        header { background: #2c3e50; color: white; padding: 1rem 0; }
        .navbar { display: flex; justify-content: space-between; align-items: center; }
        .logo { font-size: 1.5rem; font-weight: bold; color: white; text-decoration: none; }
        .nav-links { display: flex; list-style: none; gap: 2rem; }
        .nav-links a { color: white; text-decoration: none; }
        .auth-buttons { display: flex; gap: 1rem; }
        .btn { padding: 0.5rem 1rem; border: none; border-radius: 4px; text-decoration: none; }
        .btn-primary { background: #3498db; color: white; }
        .btn-outline { background: transparent; color: white; border: 1px solid white; }
        main { padding: 2rem 0; min-height: 70vh; }
        .card { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .alert { padding: 1rem; margin: 1rem 0; border-radius: 4px; }
        .alert-error { background: #e74c3c; color: white; }
        .alert-success { background: #2ecc71; color: white; }
        footer { background: #34495e; color: white; text-align: center; padding: 2rem 0; margin-top: 2rem; }
        .form-group { margin-bottom: 1rem; }
        label { display: block; margin-bottom: 0.5rem; }
        input, select { width: 100%; padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; }
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
</body>
</html>