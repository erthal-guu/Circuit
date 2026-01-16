function openModal() {
    if (modal) {
        modal.classList.add('active');
        modal.style.display = 'flex';
    }
    if (msgDiv) {
        msgDiv.innerText = "";
        msgDiv.style.color = "";
    }
}

function closeModal() {
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}