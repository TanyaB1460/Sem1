<#assign title="Главная">
<#include "base.ftlh">

<@base>
    <div class="card">
        <h1>Добро пожаловать в LangExchange! 🌍</h1>
        <p style="font-size: 1.1rem; margin: 1rem 0;">
            Найди языкового партнера и улучшай свои навыки через живое общение!
        </p>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem; margin: 2rem 0;">
            <div style="text-align: center; padding: 1rem; background: #f8f9fa; border-radius: 8px;">
                <h3>🎯 Найди партнера</h3>
                <p>Подбери собеседника по интересам и уровню языка</p>
            </div>

            <div style="text-align: center; padding: 1rem; background: #f8f9fa; border-radius: 8px;">
                <h3>💬 Общайся свободно</h3>
                <p>Практикуй язык в неформальной обстановке</p>
            </div>

            <div style="text-align: center; padding: 1rem; background: #f8f9fa; border-radius: 8px;">
                <h3>🚀 Совершенствуйся</h3>
                <p>Улучшай разговорные навыки с носителями</p>
            </div>
        </div>

        <div style="text-align: center; margin-top: 2rem;">
            <#if user??>
                <a href="/search" class="btn btn-primary" style="font-size: 1.1rem; padding: 0.8rem 1.5rem;">
                    Найти партнера для общения
                </a>
            <#else>
                <a href="/register" class="btn btn-primary" style="font-size: 1.1rem; padding: 0.8rem 1.5rem;">
                    Начать бесплатно
                </a>
                <p style="margin-top: 1rem;">Уже есть аккаунт? <a href="/login">Войдите</a></p>
            </#if>
        </div>
    </div>

    <div class="card" style="margin-top: 2rem;">
        <h2>Популярные языки для изучения</h2>
        <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; margin-top: 1rem;">
            <span style="background: #3498db; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Английский</span>
            <span style="background: #e74c3c; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Испанский</span>
            <span style="background: #9b59b6; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Французский</span>
            <span style="background: #f39c12; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Немецкий</span>
            <span style="background: #1abc9c; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Китайский</span>
            <span style="background: #34495e; color: white; padding: 0.4rem 0.8rem; border-radius: 15px;">Японский</span>
        </div>
    </div>
</@base>