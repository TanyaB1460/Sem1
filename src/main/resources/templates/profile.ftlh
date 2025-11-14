<#assign title="Мой профиль">
<#include "base.ftlh">

<@base>
    <div class="card">
        <div style="display: flex; justify-content: between; align-items: center; margin-bottom: 2rem;">
            <h1>Мой профиль</h1>
            <a href="/search" class="btn btn-primary">🔍 Найти партнера</a>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 2rem;">
            <!-- Основная информация -->
            <div>
                <h3>Основная информация</h3>
                <div style="background: #f8f9fa; padding: 1.5rem; border-radius: 8px;">
                    <div style="text-align: center; margin-bottom: 1rem;">
                        <div style="font-size: 3rem; margin-bottom: 0.5rem;">👤</div>
                        <strong style="font-size: 1.2rem;">${user.username}</strong>
                    </div>

                    <div class="profile-info">
                        <div class="info-item">
                            <strong>Email:</strong>
                            <span>${user.email}</span>
                        </div>
                        <div class="info-item">
                            <strong>Родной язык:</strong>
                            <span>${user.nativeLanguage!"Не указан"}</span>
                        </div>
                        <div class="info-item">
                            <strong>Изучаемый язык:</strong>
                            <span>${user.learningLanguage!"Не указан"}</span>
                        </div>
                        <div class="info-item">
                            <strong>Уровень:</strong>
                            <span>
                                <#if user.level == "beginner">Начинающий (A1-A2)
                                <#elseif user.level == "intermediate">Средний (B1-B2)
                                <#elseif user.level == "advanced">Продвинутый (C1-C2)
                                <#elseif user.level == "native">Носитель
                                <#else>Не указан
                                </#if>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Интересы и действия -->
            <div>
                <h3>Мои интересы</h3>
                <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; margin-bottom: 2rem;">
                    <#if userInterests?? && userInterests?size gt 0>
                        <#list userInterests as interest>
                            <span class="interest-tag">${interest.name}</span>
                        </#list>
                    <#else>
                        <p style="color: #666;">Интересы не выбраны</p>
                    </#if>
                </div>

                <h3>Быстрые действия</h3>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <a href="/search" class="action-card">
                        <div style="font-size: 2rem; margin-bottom: 0.5rem;">🔍</div>
                        <strong>Найти партнера</strong>
                        <p>Поиск по языкам и интересам</p>
                    </a>

                    <a href="/sessions" class="action-card">
                        <div style="font-size: 2rem; margin-bottom: 0.5rem;">💬</div>
                        <strong>Мои сессии</strong>
                        <p>Управление языковыми сессиями</p>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Статистика -->
    <div class="card">
        <h3>Статистика обучения</h3>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 1rem; text-align: center;">
            <div class="stat-card">
                <div class="stat-number" id="sessionsCount">0</div>
                <div class="stat-label">Сессий проведено</div>
            </div>
            <div class="stat-card">
                <div class="stat-number" id="partnersCount">0</div>
                <div class="stat-label">Партнеров найдено</div>
            </div>
            <div class="stat-card">
                <div class="stat-number" id="hoursCount">0</div>
                <div class="stat-label">Часов практики</div>
            </div>
        </div>
    </div>
</@base>

<style>
    .profile-info {
        display: flex;
        flex-direction: column;
        gap: 0.8rem;
    }

    .info-item {
        display: flex;
        justify-content: space-between;
        padding: 0.5rem 0;
        border-bottom: 1px solid #eee;
    }

    .info-item:last-child {
        border-bottom: none;
    }

    .interest-tag {
        background: #3498db;
        color: white;
        padding: 0.4rem 0.8rem;
        border-radius: 15px;
        font-size: 0.9rem;
    }

    .action-card {
        background: #f8f9fa;
        padding: 1.5rem;
        border-radius: 8px;
        text-decoration: none;
        color: inherit;
        text-align: center;
        transition: transform 0.2s, background 0.2s;
        border: 2px solid transparent;
    }

    .action-card:hover {
        transform: translateY(-2px);
        background: #e9ecef;
        border-color: #3498db;
    }

    .stat-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 1.5rem;
        border-radius: 8px;
    }

    .stat-number {
        font-size: 2rem;
        font-weight: bold;
        margin-bottom: 0.5rem;
    }

    .stat-label {
        font-size: 0.9rem;
        opacity: 0.9;
    }
</style>

<script>
    // Анимация счетчиков статистики
    function animateCounter(element, target, duration = 2000) {
        let start = 0;
        const increment = target / (duration / 16);

        const timer = setInterval(() => {
            start += increment;
            if (start >= target) {
                element.textContent = target;
                clearInterval(timer);
            } else {
                element.textContent = Math.floor(start);
            }
        }, 16);
    }

    // Загрузка статистики (заглушка - в реальном приложении нужно получать с сервера)
    document.addEventListener('DOMContentLoaded', function() {
        // Симуляция загрузки данных
        setTimeout(() => {
            animateCounter(document.getElementById('sessionsCount'), 12);
            animateCounter(document.getElementById('partnersCount'), 5);
            animateCounter(document.getElementById('hoursCount'), 8);
        }, 500);


    });
</script>