<#assign title="Мои языковые сессии">
<#include "base.ftlh">

<@base>
    <div class="card">
        <div style="display: flex; justify-content: between; align-items: center; margin-bottom: 2rem;">
            <h1>🗓️ Мои языковые сессии</h1>
            <a href="/sessions?action=create" class="btn btn-primary">+ Новая сессия</a>
        </div>

        <!-- Сообщения об успехе/ошибке -->
        <#if request.getParameter('success')??>
            <div class="alert alert-success">
                <#if request.getParameter('success') == "created">✅ Сессия успешно создана!
                <#elseif request.getParameter('success') == "updated">✅ Сессия успешно обновлена!
                <#elseif request.getParameter('success') == "deleted">✅ Сессия успешно удалена!
                <#elseif request.getParameter('success') == "completed">✅ Сессия отмечена как завершенная!
                </#if>
            </div>
        </#if>

        <#if request.getParameter('error')??>
            <div class="alert alert-error">
                <#if request.getParameter('error') == "access_denied">❌ Доступ запрещен!
                <#elseif request.getParameter('error') == "invalid_id">❌ Неверный ID сессии!
                <#elseif request.getParameter('error') == "invalid_data">❌ Неверные данные!
                </#if>
            </div>
        </#if>

        <!-- Статистика -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-bottom: 2rem;">
            <div class="stat-card">
                <div class="stat-number" id="totalSessions">0</div>
                <div class="stat-label">Всего сессий</div>
            </div>
            <div class="stat-card">
                <div class="stat-number" id="plannedSessions">0</div>
                <div class="stat-label">Запланировано</div>
            </div>
            <div class="stat-card">
                <div class="stat-number" id="completedSessions">0</div>
                <div class="stat-label">Завершено</div>
            </div>
            <div class="stat-card">
                <div class="stat-number" id="totalHours">0</div>
                <div class="stat-label">Часов практики</div>
            </div>
        </div>

        <!-- Список сессий -->
        <#if sessions?? && sessions?size gt 0>
            <div class="sessions-list">
                <#list sessions as session>
                    <div class="session-card" data-status="${session.status}">
                        <div class="session-header">
                            <div class="session-title">
                                <h3>${session.title}</h3>
                                <span class="session-status" style="background: ${session.statusColor}">
                                    ${session.statusDisplay}
                                </span>
                            </div>
                            <div class="session-actions">
                                <#if session.status == "planned">
                                    <button class="btn btn-success btn-small"
                                            onclick="completeSession(${session.id})">
                                        ✅ Завершить
                                    </button>
                                </#if>
                                <a href="/sessions?action=edit&id=${session.id}"
                                   class="btn btn-outline btn-small">✏️ Редактировать</a>
                                <button class="btn btn-danger btn-small"
                                        onclick="deleteSession(${session.id})">
                                    🗑️ Удалить
                                </button>
                            </div>
                        </div>

                        <div class="session-details">
                            <div class="detail-item">
                                <span class="detail-label">🗣️ Язык:</span>
                                <span class="detail-value">${session.language!"Не указан"}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">📅 Дата и время:</span>
                                <span class="detail-value">${session.scheduledTime!""}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">⏱️ Длительность:</span>
                                <span class="detail-value">${session.durationMinutes!"0"} минут</span>
                            </div>
                            <#if session.description??>
                                <div class="detail-item">
                                    <span class="detail-label">📝 Описание:</span>
                                    <span class="detail-value">${session.description}</span>
                                </div>
                            </#if>
                        </div>

                        <div class="session-footer">
                            <span class="session-date">
                                Создано: ${session.createdAt!""}
                            </span>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div style="text-align: center; padding: 3rem; color: #666;">
                <div style="font-size: 4rem; margin-bottom: 1rem;">📅</div>
                <h3>У вас пока нет языковых сессий</h3>
                <p>Создайте первую сессию для практики языка</p>
                <a href="/sessions?action=create" class="btn btn-primary" style="margin-top: 1rem;">
                    + Создать первую сессию
                </a>
            </div>
        </#if>
    </div>
</@base>

<style>
    .sessions-list {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    .session-card {
        background: white;
        border: 2px solid #e1e5e9;
        border-radius: 10px;
        padding: 1.5rem;
        transition: all 0.3s ease;
    }

    .session-card:hover {
        border-color: #3498db;
        box-shadow: 0 4px 12px rgba(52, 152, 219, 0.1);
    }

    .session-card[data-status="completed"] {
        border-left: 4px solid #27ae60;
        opacity: 0.8;
    }

    .session-card[data-status="planned"] {
        border-left: 4px solid #3498db;
    }

    .session-card[data-status="cancelled"] {
        border-left: 4px solid #e74c3c;
        opacity: 0.6;
    }

    .session-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 1rem;
    }

    .session-title {
        flex: 1;
    }

    .session-title h3 {
        margin: 0 0 0.5rem 0;
        color: #2c3e50;
    }

    .session-status {
        padding: 0.3rem 0.8rem;
        border-radius: 15px;
        color: white;
        font-size: 0.8rem;
        font-weight: 500;
    }

    .session-actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
    }

    .session-details {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 1rem;
        margin-bottom: 1rem;
        padding: 1rem;
        background: #f8f9fa;
        border-radius: 8px;
    }

    .detail-item {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
    }

    .detail-label {
        font-weight: 600;
        color: #666;
        font-size: 0.9rem;
    }

    .detail-value {
        color: #2c3e50;
    }

    .session-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 1rem;
        border-top: 1px solid #e1e5e9;
        font-size: 0.8rem;
        color: #7f8c8d;
    }

    .stat-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 1.5rem;
        border-radius: 10px;
        text-align: center;
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

    .btn-small {
        padding: 0.4rem 0.8rem;
        font-size: 0.85rem;
    }

    .btn-success {
        background: #27ae60;
        color: white;
        border: none;
    }

    .btn-danger {
        background: #e74c3c;
        color: white;
        border: none;
    }
</style>

<script>
    // Подсчет статистики
    function calculateStats() {
        const sessions = document.querySelectorAll('.session-card');
        let total = sessions.length;
        let planned = 0;
        let completed = 0;
        let totalHours = 0;

        sessions.forEach(session => {
            const status = session.getAttribute('data-status');
            if (status === 'planned') planned++;
            if (status === 'completed') completed++;

            // Подсчет часов (примерно)
            const durationText = session.querySelector('.detail-value')?.textContent;
            if (durationText && durationText.includes('минут')) {
                const minutes = parseInt(durationText) || 0;
                totalHours += Math.floor(minutes / 60);
            }
        });

        // Анимация счетчиков
        animateCounter(document.getElementById('totalSessions'), total);
        animateCounter(document.getElementById('plannedSessions'), planned);
        animateCounter(document.getElementById('completedSessions'), completed);
        animateCounter(document.getElementById('totalHours'), totalHours);
    }

    function animateCounter(element, target, duration = 1000) {
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

    // Управление сессиями
    function deleteSession(sessionId) {
        if (confirm('Вы уверены, что хотите удалить эту сессию?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/sessions';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            form.appendChild(actionInput);

            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'id';
            idInput.value = sessionId;
            form.appendChild(idInput);

            document.body.appendChild(form);
            form.submit();
        }
    }

    function completeSession(sessionId) {
        if (confirm('Отметить сессию как завершенную?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/sessions';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'complete';
            form.appendChild(actionInput);

            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'id';
            idInput.value = sessionId;
            form.appendChild(idInput);

            document.body.appendChild(form);
            form.submit();
        }
    }

    // Инициализация при загрузке
    document.addEventListener('DOMContentLoaded', function() {
        calculateStats();
    });
</script>