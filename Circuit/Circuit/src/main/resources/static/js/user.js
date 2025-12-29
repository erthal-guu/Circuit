    function openModal() {
        document.getElementById('userModal').classList.add('active');
        document.getElementById('modalTitle').textContent = 'Novo Usuário';
        document.getElementById('userForm').reset();
    }

    function closeModal() {
        document.getElementById('userModal').classList.remove('active');
    }
    document.getElementById('userCPF').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        }
        e.target.value = value;
    });
    document.getElementById('userPhone').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/^(\d{2})(\d)/, '($1) $2');
            value = value.replace(/(\d{5})(\d)/, '$1-$2');
        }
        e.target.value = value;
    });

    window.onclick = function(event) {
        const modal = document.getElementById('userModal');
        if (event.target === modal) {
            closeModal();
        }
    }