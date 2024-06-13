const navGuest = document.getElementById("nav-guest");
const navMember = document.getElementById("nav-member");
const displayNickname = document.getElementById("display-nickname");

fetch("/member/check-login")
  .then((response) => {
    if (response.ok) {
      return response.json();
    }
    throw new Error("로그인 상태를 확인하는 중에 오류 발생");
  })
  .then((data) => {
    if (data.name) {
      navGuest.style.display = "none";
      displayNickname.textContent = `Hello, ${data.name}! 👋`;
      console.log("사용자 이름:", data.name);
    } else {
      navMember.style.display = "none";
      displayNickname.style.display = "none";
    }
  })
  .catch((error) => {
    console.error("오류 발생:", error);
  });
