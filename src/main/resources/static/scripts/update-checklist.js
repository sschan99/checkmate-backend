document.addEventListener('DOMContentLoaded', function () {
    const addBtn = document.getElementById('addBtn');
    const checklist = document.getElementById('checklist');
    const createBtn = document.getElementById('createBtn');
    const aiHelpBtn = document.getElementById('aiHelpBtn');

    addBtn.addEventListener('click', function () {
        addItem('', false);
    });

    createBtn.addEventListener('click', function () {
        const titleInput = document.getElementById('input-title');
        const itemsContainer = document.getElementsByClassName('checklist-item-container');
        const data = [];

        for (let i = 0; i < itemsContainer.length; i++) {
            const itemCheckbox = itemsContainer[i].querySelector('.checklist-item-checkbox');
            const itemInput = itemsContainer[i].querySelector('.checklist-item-input');

            const item = {
                text: itemInput.value, checked: itemCheckbox.checked
            };

            data.push(item);
        }

        const payload = {
            title: titleInput.value, content: data
        };

        const xhr = new XMLHttpRequest();
        const url = document.getElementsByClassName('post-form').item(0).getAttribute('action');
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                console.log('요청이 성공했습니다.');
                window.location.href = '/';
            } else {
                console.error('요청이 실패했습니다.');
            }
        };
        xhr.onerror = function () {
            console.error('네트워크 오류가 발생했습니다.');
        };
        xhr.send(JSON.stringify(payload));
    });

    aiHelpBtn.addEventListener('click', async function () {
        const aiInput = document.getElementById('ai-input');
        const place = aiInput.value;

        if (!place) {
            alert('장소를 입력하세요.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:3000/travel?place=${encodeURIComponent(place)}`);
            const data = await response.json();

            Object.keys(data).forEach(category => {
                data[category].forEach(item => {
                    addItem(`[${category}] ${item}`, false);
                });
            });
        } catch (error) {
            console.error('AI 도움 요청 실패:', error);
            alert('AI 도움 요청에 실패했습니다.');
        }
    });

    function addItem(text, checked) {
        const newItemContainer = document.createElement('div');
        newItemContainer.classList.add('checklist-item-container');

        const newItemCheckbox = document.createElement('input');
        newItemCheckbox.type = 'checkbox';
        newItemCheckbox.classList.add('checklist-item-checkbox');
        newItemCheckbox.checked = checked;

        const newItemInput = document.createElement('input');
        newItemInput.type = 'text';
        newItemInput.placeholder = '체크박스 내용 입력';
        newItemInput.classList.add('checklist-item-input');
        newItemInput.value = text;

        const deleteButton = document.createElement('div');
        deleteButton.textContent = '삭제';
        deleteButton.classList.add('delete-button');

        newItemContainer.appendChild(newItemCheckbox);
        newItemContainer.appendChild(newItemInput);
        newItemContainer.appendChild(deleteButton);

        newItemContainer.addEventListener('mouseenter', function () {
            deleteButton.style.display = 'inline-block';
        });

        newItemContainer.addEventListener('mouseleave', function () {
            deleteButton.style.display = 'none';
        });

        deleteButton.addEventListener('click', function () {
            newItemContainer.remove();
        });

        checklist.appendChild(newItemContainer);
    }
})
