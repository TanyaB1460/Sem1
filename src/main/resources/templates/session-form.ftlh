<#assign title=(formAction == "create")?then("Создание сессии", "Редактирование сессии")>
<#include "base.ftlh">

<@base>
    <div class="card" style="max-width: 600px; margin: 0 auto;">
        <h1>
            <#if formAction == "create">➕ Создание языковой сессии
            <#else>✏️ Редактирование сессии
            </#if>
        </h1>

        <form id="sessionForm" method="post" action="/sessions" onsubmit="return validateSessionForm()">
            <input type="hidden" name="action" value="${formAction}">
            <#if formAction == "edit" && session??>
                <input type="hidden" name="id" value="${session.id}">
            </#if>

            <div class="form-group">
                <label for="title">Название сессии *</label>
                <input type="text" id="title" name="title"
                       value="${(session.title)!""}"
                       placeholder="Например: Разговорная практика английского"
                       required maxlength="100">
            </div>

            <div class="form-group">
                <label for="language">Язык практики *</label>
                <select id="language" name="language" required>
                    <option value="">Выберите язык</option>
                    <option value="english" <#if (session.language)?? && session.language == "english">selected</#if>>Английский</option>
                    <option value="russian" <#if (session.language)?? && session.language == "russian">selected</#if>>Русский</option>
                    <option value="spanish" <#if (session.language)?? && session.language == "spanish">selected</#if>>Испанский</option>
                    <option value="french" <#if (session.language)?? && session.language == "french">selected</#if>>Французский</option>
                    <option value="german" <#if (session.language)?? && session.language == "german">selected</#if>>Немецкий</option>
                    <option value="chinese" <#if (session.language)?? && session.language == "chinese">selected</#if>>Китайский</option>
                </select>
            </div>

            <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="scheduledTime">Дата и время *</label>
                    <input type="datetime-local" id="scheduledTime" name="scheduledTime"
                           value="${(session.scheduledTime)!""}"
                           required>
                </div>

                <div class="form-group">
                    <label for="duration">Длительность (мин) *</label>
                    <input type="number" id="duration" name="duration"
                           value="${(session.durationMinutes)!60}"
                           min="15" max="240" step="15" required>
                </div>
            </div>

            <div class="form-group">
                <label for="partnerId">ID партнера (опционально)</label>
                <input type="number" id="partnerId" name="partnerId"
                       value="${(session.partnerId)!""}"
                       placeholder="ID пользователя из поиска">
                <small>Можно найти ID в результатах поиска партнеров</small>
            </div>

            <div class="form-group">
                <label for="description">Описание сессии</label>
                <textarea id="description" name="description"
                          rows="4"
                          placeholder="О чем планируете говорить? Какие темы хотите обсудить?">${(session.description)!""}</textarea>
            </div>

            <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                <button type="submit" class="btn btn-primary">
                    <#if formAction == "create">✅ Создать сессию
                    <#else>💾 Сохранить изменения
                    </#if>
                </button>
                <a href="/sessions" class="btn btn-outline">❌ Отмена</a>
            </div>
        </form>
    </div>
</@base>

<style>
    .form-group {
        margin-bottom: 1.5rem;
    }

    label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: 600;
        color: #2c3e50;
    }

    input, select, textarea {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e1e5e9;
        border-radius: 6px;
        font-size: 1rem;
        transition: border-color 0.3s;
    }

    input:focus, select:focus, textarea:focus {
        outline: none;
        border-color: #3498db;
    }

    small {
        color: #7f8c8d;
        font-size: 0.8rem;
        margin-top: 0.3rem;
        display: block;
    }
</style>

<script>
    function validateSessionForm() {
        const title = document.getElementById('title').value.trim();
        const language = document.getElementById('language').value;
        const scheduledTime = document.getElementById('scheduledTime').value;
        const duration = document.getElementById('duration').value;

        // Проверка названия
        if (title.length < 3) {
            alert('Название сессии должно содержать минимум 3 символа');
            return false;
        }

        // Проверка языка
        if (!language) {
            alert('Пожалуйста, выберите язык для практики');
            return false;
        }

        // Проверка даты
        if (!scheduledTime) {
            alert('Пожалуйста, укажите дату и время сессии');
            return false;
        }

        // Проверка что дата не в прошлом
        const selectedDate = new Date(scheduledTime);
        const now = new Date();
        if (selectedDate < now) {
            alert('Нельзя планировать сессию в прошлом!');
            return false;
        }

        // Проверка длительности
        if (duration < 15 || duration > 240) {
            alert('Длительность сессии должна быть от 15 до 240 минут');
            return false;
        }

        return true;
    }

    // Установка минимальной даты (текущее время)
    document.addEventListener('DOMContentLoaded', function() {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        document.getElementById('scheduledTime').min = now.toISOString().slice(0, 16);
    });
</script>