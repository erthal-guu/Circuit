function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    const tabId = 'tab' + tabName.charAt(0).toUpperCase() + tabName.slice(1);
    const targetDiv = document.getElementById(tabId);
    if (targetDiv) {
        targetDiv.style.display = 'block';
    }
    if (event) {
        event.currentTarget.classList.add('active');
    }
}
document.addEventListener("DOMContentLoaded", function() {
    const tabTodas = document.getElementById('tabTodas');
    if(tabTodas) tabTodas.style.display = 'block';
});

