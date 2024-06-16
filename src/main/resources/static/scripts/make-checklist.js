document.addEventListener('DOMContentLoaded', function () {
    const addBtn = document.getElementById('addBtn');
    const checklist = document.getElementById('checklist');
    const createBtn = document.getElementById('createBtn');
    const aiHelpBtn = document.getElementById('aiHelpBtn');
    const modal = document.getElementById("aiModal");
    const span = document.getElementsByClassName("close")[0];
    const generateBtn = document.getElementById("generateBtn");

    addBtn.addEventListener('click', function () {
        addItem('', false);
    });
    addBtn.click();

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
        xhr.open('POST', '/checklist/create', true);
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

    aiHelpBtn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    generateBtn.onclick = async function() {
        const travelStyle = document.getElementById("travelStyle").value;
        const interests = document.getElementById("interests").value;
        const budget = document.getElementById("budget").value;
        const duration = document.getElementById("duration").value;
        const place = document.getElementById("place").value;
        const transportation = document.getElementById("transportation").value;
        const accommodation = document.getElementById("accommodation").value;
        const companions = document.getElementById("companions").value;
        const specialNeeds = document.getElementById("specialNeeds").value;

        const queryString = new URLSearchParams({
            travelStyle,
            interests,
            budget,
            duration,
            place,
            transportation,
            accommodation,
            companions,
            specialNeeds
        }).toString();

        try {
            const response = await fetch(`http://localhost:3000/travel?${queryString}`);
            const data = await response.json();

            if (response.ok) {
                Object.keys(data).forEach(category => {
                    data[category].forEach(item => {
                        addItem(`[${category}] ${item}`, false);
                    });
                });
                modal.style.display = "none";
            } else {
                alert(data.error || "체크리스트를 생성할 수 없습니다.");
            }
        } catch (error) {
            console.error("Error fetching checklist:", error);
            alert("체크리스트를 생성하는 동안 오류가 발생했습니다.");
        }
    }

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
