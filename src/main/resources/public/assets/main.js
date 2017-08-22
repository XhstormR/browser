let filterEl = document.getElementById('filter');
filterEl.focus();

function filter() {
    let q = filterEl.value.trim().toLowerCase();
    let elems = document.querySelectorAll('tr.file');
    elems.forEach(function (el) {
        if (!q) {
            el.style.display = '';
            return;
        }
        let nameEl = el.querySelector('.name');
        let nameVal = nameEl.textContent.trim().toLowerCase();
        if (nameVal.indexOf(q) !== -1) {
            el.style.display = '';
        } else {
            el.style.display = 'none';
        }
    });
}
