<#assign title="Поиск партнеров">
<#include "base.ftlh">

<@base>
    <div class="card">
        <h1>🔍 Поиск языковых партнеров</h1>
        <p>Найдите идеального партнера для языковой практики по вашим критериям</p>

        <!-- Форма поиска -->
        <form id="searchForm" method="get" action="/search" style="margin-top: 2rem;">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.5rem;">
                <div class="form-group">
                    <label for="nativeLanguage">Родной язык партнера:</label>
                    <select id="nativeLanguage" name="nativeLanguage">
                        <option value="">Любой язык</option>
                        <option value="russian" <#if searchNativeLanguage?? && searchNativeLanguage == "russian">selected</#if>>Русский</option>
                        <option value="english" <#if searchNativeLanguage?? && searchNativeLanguage == "english">selected</#if>>Английский</option>
                        <option value="spanish" <#if searchNativeLanguage?? && searchNativeLanguage == "spanish">selected</#if>>Испанский</option>
                        <option value="french" <#if searchNativeLanguage?? && searchNativeLanguage == "french">selected</#if>>Французский</option>
                        <option value="german" <#if searchNativeLanguage?? && searchNativeLanguage == "german">selected</#if>>Немецкий</option>
                        <option value="chinese" <#if searchNativeLanguage?? && searchNativeLanguage == "chinese">selected</#if>>Китайский</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="learningLanguage">Изучаемый язык партнера:</label>
                    <select id="learningLanguage" name="learningLanguage">
                        <option value="">Любой язык</option>
                        <option value="russian" <#if searchLearningLanguage?? && searchLearningLanguage == "russian">selected</#if>>Русский</option>
                        <option value="english" <#if searchLearningLanguage?? && searchLearningLanguage == "english">selected</#if>>Английский</option>
                        <option value="spanish" <#if searchLearningLanguage?? && searchLearningLanguage == "spanish">selected</#if>>Испанский</option>
                        <option value="french" <#if searchLearningLanguage?? && searchLearningLanguage == "french">selected</#if>>Французский</option>
                        <option value="german" <#if searchLearningLanguage?? && searchLearningLanguage == "german">selected</#if>>Немецкий</option>
                        <option value="chinese" <#if searchLearningLanguage?? && searchLearningLanguage == "chinese">selected</#if>>Китайский</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label>Интересы партнера (можно выбрать несколько):</label>
                <div class="checkbox-group">
                    <#if interests??>
                        <#list interests as interest>
                            <div class="checkbox-item">
                                <input type="checkbox" id="interest-${interest.id}" name="interests"
                                       value="${interest.id}"
                                       <#if searchInterests?? && searchInterests?seq_contains(interest.id)>checked</#if>>
                                <label for="interest-${interest.id}">${interest.name}</label>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <div style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                <button type="submit" class="btn btn-primary">
                    🔍 Найти партнеров
                </button>
                <button type="button" class="btn btn-outline" onclick="clearSearch()">
                    🗑️ Очистить
                </button>
            </div>
        </form>
    </div>

    <!-- Результаты поиска -->
    <#if searchPerformed?? && searchPerformed>
        <div class="card">
            <h2>📋 Результаты поиска
                <span style="font-size: 1rem; color: #666;">
                    (найдено: <#if partners??>${partners?size}<#else>0</#if>)
                </span>
            </h2>

            <#if partners?? && partners?size gt 0>
                <div id="partnersList" class="partners-grid">
                    <#list partners as partner>
                        <div class="partner-card">
                            <div class="partner-header">
                                <div class="partner-avatar">👤</div>
                                <div class="partner-info">
                                    <h3>${partner.username}</h3>
                                    <span class="partner-level">
                                        <#if partner.level == "beginner">🎯 Начинающий
                                        <#elseif partner.level == "intermediate">⚡ Средний
                                        <#elseif partner.level == "advanced">🚀 Продвинутый
                                        <#elseif partner.level == "native">🏆 Носитель
                                        <#else>❓ Не указан
                                        </#if>
                                    </span>
                                </div>
                            </div>

                            <div class="partner-languages">
                                <div class="language-item">
                                    <span class="language-label">🗣️ Родной:</span>
                                    <span class="language-value">${partner.nativeLanguage!"Не указан"}</span>
                                </div>
                                <div class="language-item">
                                    <span class="language-label">📚 Изучает:</span>
                                    <span class="language-value">${partner.learningLanguage!"Не указан"}</span>
                                </div>
                            </div>

                            <#assign partnerInterests = partner.getInterests()![]>
                            <#if partnerInterests?size gt 0>
                                <div class="partner-interests">
                                    <strong>Интересы:</strong>
                                    <div class="interests-list">
                                        <#list partnerInterests as interest>
                                            <span class="interest-tag">${interest.name}</span>
                                        </#list>
                                    </div>
                                </div>
                            </#if>

                            <div class="partner-actions">
                                <button class="btn btn-primary btn-small" onclick="startSession(${partner.id})">
                                    💬 Начать сессию
                                </button>
                                <button class="btn btn-outline btn-small" onclick="viewProfile(${partner.id})">
                                    👀 Профиль
                                </button>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div style="text-align: center; padding: 3rem; color: #666;">
                    <div style="font-size: 4rem; margin-bottom: 1rem;">😔</div>
                    <h3>Партнеры не найдены</h3>
                    <p>Попробуйте изменить критерии поиска</p>
                </div>
            </#if>
        </div>
    </#if>
</@base>

<style>
    .partners-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 1.5rem;
        margin-top: 1.5rem;
    }

    .partner-card {
        background: white;
        border: 1px solid #e1e5e9;
        border-radius: 10px;
        padding: 1.5rem;
        transition: transform 0.2s, box-shadow 0.2s;
    }

    .partner-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .partner-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 1rem;
    }

    .partner-avatar {
        font-size: 2rem;
        width: 60px;
        height: 60px;
        background: #f8f9fa;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .partner-info h3 {
        margin: 0;
        color: #2c3e50;
    }

    .partner-level {
        font-size: 0.8rem;
        color: #7f8c8d;
    }

    .partner-languages {
        margin-bottom: 1rem;
    }

    .language-item {
        display: flex;
        justify-content: space-between;
        margin-bottom: 0.5rem;
        padding-bottom: 0.5rem;
        border-bottom: 1px solid #f8f9fa;
    }

    .language-label {
        font-weight: 500;
        color: #666;
    }

    .language-value {
        color: #2c3e50;
    }

    .partner-interests {
        margin-bottom: 1rem;
    }

    .interests-list {
        display: flex;
        flex-wrap: wrap;
        gap: 0.3rem;
        margin-top: 0.5rem;
    }

    .interest-tag {
        background: #e3f2fd;
        color: #1976d2;
        padding: 0.2rem 0.6rem;
        border-radius: 12px;
        font-size: 0.8rem;
    }

    .partner-actions {
        display: flex;
        gap: 0.5rem;
    }

    .btn-small {
        padding: 0.4rem 0.8rem;
        font-size: 0.9rem;
        flex: 1;
    }

    .checkbox-group {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 0.8rem;
        margin-top: 0.5rem;
    }
</style>

<script>
    function clearSearch() {
        document.getElementById('nativeLanguage').value = '';
        document.getElementById('learningLanguage').value = '';

        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach(checkbox => checkbox.checked = false);

        document.getElementById('searchForm').submit();
    }

    function startSession(partnerId) {
        if (confirm('Начать языковую сессию с этим партнером?')) {
            // В реальном приложении здесь будет AJAX запрос
            alert('Сессия начата! Переходим к чату...');
            // window.location.href = '/session/start?partnerId=' + partnerId;
        }
    }

    function viewProfile(partnerId) {
        // В реальном приложении переход на профиль партнера
        alert('Просмотр профиля партнера ID: ' + partnerId);
        // window.location.href = '/profile/' + partnerId;
    }

    // AJAX поиск (для будущего улучшения)
    function performSearch() {
        const formData = new FormData(document.getElementById('searchForm'));

        fetch('/search', {
            method: 'GET',
            body: new URLSearchParams(formData)
        })
            .then(response => response.text())
            .then(html => {
                // Обновляем только результаты поиска
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newResults = doc.querySelector('#partnersList');
                if (newResults) {
                    document.getElementById('partnersList').innerHTML = newResults.innerHTML;
                }
            })
            .catch(error => console.error('Search error:', error));
    }
</script>