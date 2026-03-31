/* ══════════════════════════════════════════════
   ReqSpec — auth.js
   Tab switching + password strength meter
══════════════════════════════════════════════ */

/**
 * Switch between Sign In and Register tabs.
 * @param {'login'|'register'} tab
 */
function switchTab(tab) {
    document.querySelectorAll('.tab').forEach((t, i) => {
        t.classList.toggle('active',
            (tab === 'login' && i === 0) ||
            (tab === 'register' && i === 1)
        );
    });
    document.getElementById('panel-login').classList.toggle('active', tab === 'login');
    document.getElementById('panel-register').classList.toggle('active', tab === 'register');
}

/**
 * Evaluate password strength and update the meter UI.
 * @param {string} value
 */
function checkStrength(value) {
    let score = 0;
    if (value.length >= 8) score++;
    if (value.length >= 12) score++;
    if (/[A-Z]/.test(value) && /[a-z]/.test(value)) score++;
    if (/[0-9]/.test(value)) score++;
    if (/[^A-Za-z0-9]/.test(value)) score++;

    score = Math.min(score, 4);

    const cls = score <= 1 ? 'weak' : score <= 2 ? 'medium' : 'strong';
    const labels = ['', 'Weak', 'Fair', 'Good', 'Strong'];

    ['ss1', 'ss2', 'ss3', 'ss4'].forEach((id, i) => {
        const el = document.getElementById(id);
        if (el) el.className = 's-seg' + (i < score ? ' ' + cls : '');
    });

    const label = document.getElementById('slabel');
    if (label) label.textContent = score > 0 ? `— ${labels[score]}` : '— password strength';
}
