'use strict';
(() => {
  const form = document.querySelector('#new-order');
  const list = document.querySelector('#order-list');
  const search = document.querySelector('#order-search');
  const status = document.querySelector('#order-status');
  const message = document.querySelector('#order-message');
  const refresh = document.querySelector('#refresh-orders');
  const cancelEdit = document.querySelector('#cancel-edit');
  const title = document.querySelector('#form-title');
  const submit = form.querySelector('[type=submit]');
  let editingId = null;
  const steps = {
    PENDING: ['confirm', 'Confirm'],
    CONFIRMED: ['start-baking', 'Start baking'],
    IN_PROGRESS: ['ready', 'Mark ready'],
    READY: ['complete', 'Complete']
  };
  let orders = [];
  let busy = false;
  const escape = value => String(value ?? '').replace(/[&<>"']/g, c =>
    ({'&':'&amp;', '<':'&lt;', '>':'&gt;', '"':'&quot;', "'":'&#39;'}[c]));
  const money = value => new Intl.NumberFormat('en-LK', {style:'currency', currency:'LKR'}).format(value);
  function setDateMin() {
    const now = new Date();
    form.elements.requiredDate.min = `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,'0')}-${String(now.getDate()).padStart(2,'0')}`;
  }
  async function request(path = '', options = {}) {
    const response = await fetch('/api/orders' + path, {
      ...options, headers: {'Content-Type':'application/json'}
    });
    const body = await response.json().catch(() => null);
    if (!response.ok) throw new Error(body?.message || (response.status === 400
      ? 'Check the customer ID, date and total, then try again.'
      : `The request failed (${response.status}). Please try again.`));
    if (response.status === 204) return null;
    if (!body) throw new Error('The server returned an unexpected response.');
    return body;
  }
  function render() {
    const query = search.value.trim().toLowerCase();
    const visible = orders.filter(order =>
      (!status.value || order.status === status.value) &&
      `${order.orderCode} ${order.customerId}`.toLowerCase().includes(query));
    list.innerHTML = visible.map(order => {
      const next = steps[order.status];
      return `<article class="glass order-panel">
        <div class="order-heading"><h3>${escape(order.orderCode)}</h3><strong>${escape(order.status.replaceAll('_', ' '))}</strong></div>
        <dl class="order-details">
          <dt>Customer</dt><dd>${escape(order.customerId)}</dd>
          <dt>Required date</dt><dd>${escape(order.requiredDate)}</dd>
          <dt>Total</dt><dd>${escape(money(order.totalAmount))}</dd>
        </dl>
        <p>${escape(order.specialNote || 'No special notes.')}</p>
        <div class="order-actions">
          ${order.cancellable ? `<button class="btn ghost sm" data-id="${escape(order.id)}" data-action="edit" ${busy ? 'disabled' : ''}>Edit</button>` : ''}
          ${['PENDING','CANCELLED'].includes(order.status) ? `<button class="btn ghost sm" data-id="${escape(order.id)}" data-action="delete" ${busy ? 'disabled' : ''}>Delete</button>` : ''}
          ${next ? `<button class="btn sm" data-id="${escape(order.id)}" data-action="${next[0]}" ${busy ? 'disabled' : ''}>${next[1]}</button>` : ''}
          ${order.cancellable ? `<button class="btn ghost sm" data-id="${escape(order.id)}" data-action="cancel" ${busy ? 'disabled' : ''}>Cancel order</button>` : ''}
        </div></article>`;
    }).join('') || '<p>No orders match this view.</p>';
  }
  async function load() {
    const result = await request();
    if (!Array.isArray(result)) throw new Error('The server returned an unexpected order list.');
    orders = result.sort((a,b) => b.id - a.id);
    render();
  }
  async function run(work) {
    if (busy) return;
    busy = true;
    refresh.disabled = true;
    cancelEdit.disabled = true;
    form.querySelector('button').disabled = true;
    message.textContent = '';
    render();
    try { await work(); }
    catch (error) { message.textContent = error.message || 'Unable to reach the server.'; }
    finally {
      busy = false;
      refresh.disabled = false;
      cancelEdit.disabled = false;
      form.querySelector('button').disabled = false;
      render();
    }
  }
  function resetForm() {
    editingId = null;
    form.reset();
    title.textContent = 'Create an order';
    submit.textContent = 'Create order';
    cancelEdit.hidden = true;
    setDateMin();
  }
  cancelEdit.addEventListener('click', resetForm);
  form.addEventListener('submit', event => {
    event.preventDefault();
    run(async () => {
      const data = Object.fromEntries(new FormData(form));
      data.customerId = data.customerId.trim();
      const editing = editingId !== null;
      const order = await request(editing ? `/${encodeURIComponent(editingId)}` : '',
        {method:editing ? 'PUT' : 'POST', body:JSON.stringify(data)});
      if (editing) orders = orders.map(existing => existing.id === order.id ? order : existing);
      else orders.unshift(order);
      resetForm();
      message.textContent = `Order ${order.orderCode} ${editing ? 'updated' : 'created'}.`;
    });
  });
  list.addEventListener('click', event => {
    const button = event.target.closest('button[data-action]');
    if (!button || busy) return;
    const selected = orders.find(order => String(order.id) === button.dataset.id);
    if (!selected) return;
    if (button.dataset.action === 'edit') {
      editingId = selected.id;
      for (const key of ['customerId','requiredDate','totalAmount','specialNote']) {
        form.elements[key].value = selected[key] ?? '';
      }
      title.textContent = `Edit order ${selected.orderCode}`;
      submit.textContent = 'Save changes';
      cancelEdit.hidden = false;
      setDateMin();
      form.elements.customerId.focus();
      return;
    }
    if (button.dataset.action === 'delete') {
      if (!window.confirm(`Permanently delete order ${selected.orderCode}? This cannot be undone.`)) return;
      run(async () => {
        await request(`/${encodeURIComponent(selected.id)}`, {method:'DELETE'});
        orders = orders.filter(order => order.id !== selected.id);
        if (editingId === selected.id) resetForm();
        message.textContent = `Order ${selected.orderCode} deleted.`;
      });
      return;
    }
    if (button.dataset.action === 'cancel' && !window.confirm('Cancel this order?')) return;
    run(async () => {
      const updated = await request(`/${encodeURIComponent(button.dataset.id)}/${button.dataset.action}`, {method:'PUT'});
      orders = orders.map(order => order.id === updated.id ? updated : order);
      if (editingId === updated.id) resetForm();
      message.textContent = `Order ${updated.orderCode} updated.`;
    });
  });
  search.addEventListener('input', render);
  status.addEventListener('change', render);
  refresh.addEventListener('click', () => run(load));
  setDateMin();
  run(load);
})();
