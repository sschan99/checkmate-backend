const tableSize = document.getElementById("table-size");
const postTable = document.getElementById("post-table");

const formatDate = (time) => {
  const date = new Date(time);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  return `${year}. ${month}. ${day}.`;
};

fetch("/checklist/all")
  .then((response) => {
    if (response.ok) {
      return response.json();
    }
    throw new Error("체크리스트 목록을 불러오는 중에 오류 발생");
  })
  .then((data) => {
    tableSize.textContent = `전체 글 ${data.length}개`;
    data.forEach((post) => {
      const div = document.createElement("div");
      div.className = "table-element-wrapper";
      div.innerHTML = `
        <a href="/checklist/${post.id}" class="table-element">
            <div class="table-title">${post.title}</div>
            <div class="table-other">${post.member.name}</div>
            <div class="table-other">${formatDate(post.createdAt)}</div>
            <div class="table-other">${post.views}</div>
        </a>
        <div class="line"></div>
      `;
      postTable.append(div);
    });
  })
  .catch((error) => {
    console.error("오류 발생:", error);
  });
