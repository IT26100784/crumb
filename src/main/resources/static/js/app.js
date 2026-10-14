/* Crumb front-end · Spring Boot hookup
 * Put this folder's contents in src/main/resources/static/. All pages call the JSON endpoints below
 * (same origin, so no CORS needed). If Spring Security is on, permit "/api/**" or send the CSRF token.
 * Form fields are plain strings (dates are yyyy-MM-dd, rating is 1-5).
 *   GET  /api/reviews        -> [{id,name,type,rating,comment,createdAt}]
 *   POST /api/reviews        {name,type,rating,comment}
 *   POST /api/custom-orders  {occasion,size,date,flavor,frosting,dietary,message,notes,fulfilment,name,phone,email}
 *   POST /api/contact        {name,email,subject,message}
 *   GET  /api/cake-options   -> {size:{"Small (6–8)":4500,...},flavor:{..},frosting:{..},dietary:{..},fulfilment:{..}}  (optional price list for the custom cake form; label -> price)
 *   GET  /api/products       -> [{id,name,category,description,price,image}]
 *   POST /api/orders         {name,phone,email,fulfilment,date,address,notes,items:[{productId,quantity}]}
 *   (Orders send only productId + quantity: recalculate prices on the server, never trust the browser.)
 *   POST /api/auth/register  {name,email,password} -> {name,email,token?}
 *   POST /api/auth/login     {email,password}      -> {name,email,token?}  (or just set a session cookie)
 *   GET  /api/auth/me        -> {name,email}   (401 when signed out)
 *   POST /api/auth/logout
 * Auth: either return a JWT as "token" (sent back as a Bearer header) or use Spring session cookies.
 * Permit "/api/auth/**" in Spring Security and hash passwords with BCrypt. Errors: return {message} with 401/409/400.
 * Set DEMO:false once the backend is live (DEMO fakes success when the API can't be reached). */
const CONFIG={API:'/api',DEMO:true,CUR:'LKR\u00a0'};
const root=document.documentElement;
root.dataset.theme=localStorage.getItem('crumb-theme')||(matchMedia('(prefers-color-scheme:dark)').matches?'dark':'light');
const $=(s,r=document)=>r.querySelector(s);
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const ICON={
 chevL:'<path d="M15 18l-6-6 6-6"/>',
 chevR:'<path d="M9 18l6-6-6-6"/>',
 bag:'<path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4zM3 6h18M16 10a4 4 0 01-8 0"/>',
 user:'<circle cx="12" cy="8" r="4"/><path d="M4 21a8 8 0 0116 0"/>',
 x:'<path d="M18 6L6 18M6 6l12 12"/>',
 sun:'<circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4.9 4.9l1.4 1.4M17.7 17.7l1.4 1.4M2 12h2M20 12h2M4.9 19.1l1.4-1.4M17.7 6.3l1.4-1.4"/>',
 moon:'<path d="M21 12.8A9 9 0 1111.2 3a7 7 0 009.8 9.8z"/>',
 menu:'<path d="M4 7h16M4 12h16M4 17h16"/>',
 leaf:'<path d="M11 20A7 7 0 019.8 6.1C15.5 5 17 4.5 19 2c1 2 2 4.2 2 8 0 5.5-4.8 10-10 10z"/><path d="M2 21c0-3 1.9-5.4 5-6 2.5-.5 5-2 6-3"/>',
 clock:'<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
 lock:'<rect x="4" y="11" width="16" height="10" rx="2"/><path d="M8 11V7a4 4 0 018 0v4"/>',
 cake:'<path d="M20 21v-8a2 2 0 00-2-2H6a2 2 0 00-2 2v8M4 16s.5-1 2-1 2.5 2 4 2 2.5-2 4-2 2.5 2 4 2 2-1 2-1M2 21h20M7 8v3M12 8v3M17 8v3M7 4h.01M12 4h.01M17 4h.01"/>',
 cookie:'<path d="M12 3a9 9 0 109 9 5 5 0 01-5-5 4 4 0 01-4-4z"/><path d="M8.5 8.5v.01M16 15.5v.01M12 12v.01M11 17v.01M7 14v.01"/>',
 gift:'<rect x="3" y="8" width="18" height="4" rx="1"/><path d="M12 8v13M19 12v7a2 2 0 01-2 2H7a2 2 0 01-2-2v-7M7.5 8a2.5 2.5 0 010-5C11 3 12 8 12 8s1-5 4.5-5a2.5 2.5 0 010 5"/>',
 pin:'<path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0116 0z"/><circle cx="12" cy="10" r="3"/>',
 phone:'<path d="M22 16.9v3a2 2 0 01-2.2 2 19.8 19.8 0 01-8.6-3.1 19.5 19.5 0 01-6-6A19.8 19.8 0 012.1 4.2 2 2 0 014.1 2h3a2 2 0 012 1.7c.1 1 .4 1.9.7 2.8a2 2 0 01-.5 2.1L8.1 9.9a16 16 0 006 6l1.3-1.3a2 2 0 012.1-.4c.9.3 1.8.6 2.8.7a2 2 0 011.7 2z"/>',
 mail:'<rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 6L2 7"/>'};
const icons=()=>document.querySelectorAll('[data-i]').forEach(e=>e.innerHTML=`<svg viewBox="0 0 24 24" aria-hidden="true">${ICON[e.dataset.i]}</svg>`);
let user=null;try{user=JSON.parse(localStorage.getItem('crumb-user'))}catch{}
async function api(path,opt={}){
  try{const r=await fetch(CONFIG.API+path,{...opt,headers:{'Content-Type':'application/json',...(user?.token&&{Authorization:'Bearer '+user.token}),...opt.headers}});
    if(!r.ok){const m=await r.json().catch(()=>({}));throw Object.assign(new Error(m.message||r.status),{status:r.status})}
    return r.status===204?{}:await r.json().catch(()=>({}));
  }catch(e){if(!CONFIG.DEMO||(e.status&&e.status!==404))throw e;return{demo:true}}
}
function toast(m){let t=$('.toast');if(!t){t=document.createElement('div');t.className='toast glass';document.body.append(t)}
  t.textContent=m;t.classList.add('show');clearTimeout(t._t);t._t=setTimeout(()=>t.classList.remove('show'),3400)}
const SAMPLE=[
 {name:'Ayesha',type:'Review',rating:5,comment:'The chocolate drip cake was the centrepiece of our anniversary dinner. Rich, not too sweet.'},
 {name:'Nimal',type:'Review',rating:5,comment:'Our custom birthday cake matched the sketch exactly and arrived on time.'},
 {name:'Dilini',type:'Feedback',rating:4,comment:'Lovely macarons. I would like to see more eggless options.'},
 {name:'Kasun',type:'Suggestion',rating:5,comment:'Please add a coffee and walnut cake to the menu.'}];
function renderReviews(el,list){
  el.innerHTML=list.map(r=>{const n=Math.max(0,Math.min(5,+r.rating||0));
    return `<article class="glass card"><div class="ro">${'★'.repeat(n)}${'☆'.repeat(5-n)}</div><p>${esc(r.comment)}</p><div class="who"><span class="av">${esc(String(r.name||'?')[0].toUpperCase())}</span>${esc(r.name)} · ${esc(r.type||'Review')}</div></article>`}).join('')
    ||'<p class="mute">No reviews yet. Share the first one.</p>';
}
const money=n=>CONFIG.CUR+Number(n).toLocaleString('en-US',{maximumFractionDigits:2,minimumFractionDigits:Number.isInteger(+n)?0:2});
const SAMPLE_P=[
 {id:1,name:'Chocolate Drip Cake',category:'Cakes',description:'Dark chocolate sponge, ganache drip and fresh berries.',price:5800,image:'images/hero-cake.jpg'},
 {id:2,name:'Vanilla Celebration Cake',category:'Cakes',description:'Light vanilla sponge with silky buttercream.',price:4800,image:'images/white-cake.jpg'},
 {id:3,name:'Macaron Box (12)',category:'Desserts',description:'Assorted French macarons in seasonal flavours.',price:3200,image:'images/macarons.jpg'},
 {id:4,name:'Signature Gift Hamper',category:'Gifts',description:'A woven basket of bakes, preserves and treats.',price:7500,image:'images/hamper.jpg'},
 {id:5,name:'Fudge Brownies (6)',category:'Desserts',description:'Dense, glossy and dark chocolate.',price:1800},
 {id:6,name:'Butter Croissants (4)',category:'Pastries',description:'Flaky, laminated and baked each morning.',price:1400},
 {id:7,name:'Vanilla Cupcakes (6)',category:'Desserts',description:'Whipped buttercream swirls on soft sponge.',price:2200},
 {id:8,name:'Sourdough Loaf',category:'Pastries',description:'Slow-fermented with a crisp crust.',price:1100}];
const CAT_ICON={Cakes:'cake',Desserts:'cookie',Pastries:'cookie',Gifts:'gift'};
let PRODUCTS=[];
async function loadProducts(){const d=await api('/products');return PRODUCTS=Array.isArray(d)?d:SAMPLE_P}
function renderProducts(el,list,empty='Nothing here yet.'){
  el.innerHTML=list.map(p=>`<article class="glass pc"><div class="pi">${p.image?`<img src="${esc(p.image)}" alt="${esc(p.name)}" loading="lazy">`:`<span data-i="${CAT_ICON[p.category]||'cake'}"></span>`}</div><div class="pb"><div><h3>${esc(p.name)}</h3><p class="mute">${esc(p.description)}</p></div><div class="pf"><b>${money(p.price)}</b><button class="btn sm" data-add="${esc(p.id)}">Add to cart</button></div></div></article>`).join('')||`<p class="mute">${esc(empty)}</p>`;
  icons();
}
let cart=[];try{cart=JSON.parse(localStorage.getItem('crumb-cart'))||[]}catch{}
function paintCart(){
  const n=cart.reduce((a,i)=>a+i.qty,0),b=$('#cn');if(!b)return;
  b.textContent=n;b.hidden=!n;
  $('#ci').innerHTML=cart.map(i=>`<div class="cr"><div class="th">${i.image?`<img src="${esc(i.image)}" alt="">`:'<span data-i="cake"></span>'}</div><div><b>${esc(i.name)}</b><div class="mute">${money(i.price)}</div></div><div class="qty"><button type="button" data-q="${esc(i.id)}" data-d="-1" aria-label="Remove one">−</button><span>${i.qty}</span><button type="button" data-q="${esc(i.id)}" data-d="1" aria-label="Add one">+</button></div></div>`).join('')||'<p class="mute">Your cart is empty. Browse the shop to add something sweet.</p>';
  $('#sub').textContent=$('#tt').textContent=money(cart.reduce((a,i)=>a+i.price*i.qty,0));
  $('#chk').disabled=!n;icons();
}
const cartSave=()=>{localStorage.setItem('crumb-cart',JSON.stringify(cart));paintCart()};
document.addEventListener('click',e=>{
  const a=e.target.closest('[data-add]');
  if(a){const p=PRODUCTS.find(x=>String(x.id)===a.dataset.add);if(!p)return;const c=cart.find(x=>x.id==p.id);c?c.qty++:cart.push({id:p.id,name:p.name,price:p.price,image:p.image,qty:1});cartSave();toast(p.name+' added to cart');return}
  const q=e.target.closest('[data-q]');
  if(q){const c=cart.find(x=>String(x.id)===q.dataset.q);if(!c)return;c.qty+=+q.dataset.d;if(c.qty<1)cart=cart.filter(x=>x!==c);cartSave()}
});
const iso=d=>`${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;
const fmtDate=s=>s?new Date(s+'T00:00').toLocaleDateString('en-GB',{day:'numeric',month:'short',year:'numeric'}):'';
const closeDD=except=>document.querySelectorAll('.dd-l:not([hidden])').forEach(l=>{if(l.parentElement!==except)l.hidden=true});
document.addEventListener('click',e=>closeDD(e.composedPath().find(n=>n.classList?.contains('dd'))));
function enhanceSelect(s){ /* swaps a native <select> for a styled dropdown; the real select stays in the form */
  const w=document.createElement('div'),b=document.createElement('button'),l=document.createElement('ul');
  w.className='dd';b.type='button';b.className='dd-b';b.setAttribute('aria-haspopup','listbox');
  l.className='dd-l glass';l.setAttribute('role','listbox');l.hidden=true;
  s.before(w);w.append(s,b,l);s.classList.add('nat');
  const sync=()=>{b.textContent=s.selectedOptions[0]?.text||'';l.innerHTML=[...s.options].map((o,k)=>`<li role="option" data-k="${k}" aria-selected="${o.selected}">${esc(o.text)}</li>`).join('')};
  const pick=k=>{s.selectedIndex=k;s.dispatchEvent(new Event('change',{bubbles:true}));sync()};
  b.onclick=()=>{const o=l.hidden;closeDD();l.hidden=!o;if(o)l.querySelector('[aria-selected=true]')?.scrollIntoView({block:'nearest'})};
  l.onclick=e=>{e.preventDefault();const li=e.target.closest('li');if(li){pick(+li.dataset.k);l.hidden=true;b.focus()}};
  b.onkeydown=e=>{const d={ArrowDown:1,ArrowUp:-1}[e.key];
    if(d){e.preventDefault();pick(Math.max(0,Math.min(s.options.length-1,s.selectedIndex+d)))}
    else if(e.key==='Escape'&&!l.hidden){l.hidden=true;e.stopPropagation()}};
  s.form?.addEventListener('reset',()=>setTimeout(sync));
  sync();
}
function enhanceDate(inp){ /* swaps <input type=date> for a calendar popup; the form still gets yyyy-MM-dd */
  const w=document.createElement('div'),d=document.createElement('input'),h=document.createElement('input'),p=document.createElement('div');
  w.className='dd dp';d.className='dp-i';d.type='text';d.placeholder='Select a date';d.autocomplete='off';d.inputMode='none';d.required=inp.required;
  h.type='hidden';h.name=inp.name;p.className='dd-l glass cal';p.hidden=true;p.tabIndex=-1;
  inp.replaceWith(w);w.append(d,h,p);
  const today=new Date(),min=iso(today);let v=today;
  const draw=()=>{const y=v.getFullYear(),m=v.getMonth(),f0=new Date(y,m,1).getDay(),n=new Date(y,m+1,0).getDate(),cur=y===today.getFullYear()&&m===today.getMonth();
    let c='<i></i>'.repeat(f0);
    for(let k=1;k<=n;k++){const s=iso(new Date(y,m,k));c+=`<button type="button" data-d="${s}"${s<min?' disabled':''}${s===h.value?' class="sel"':s===min?' class="td"':''}>${k}</button>`}
    p.innerHTML=`<div class="ch"><button type="button" data-m="-1" aria-label="Previous month"${cur?' disabled':''}>‹</button><b>${v.toLocaleDateString('en-GB',{month:'long',year:'numeric'})}</b><button type="button" data-m="1" aria-label="Next month">›</button></div><div class="cw">${[...'SMTWTFS'].map(x=>`<span>${x}</span>`).join('')}</div><div class="cg">${c}</div>`};
  const open=()=>{closeDD();v=h.value?new Date(h.value+'T00:00'):new Date();draw();p.hidden=false};
  d.onclick=()=>p.hidden?open():(p.hidden=true);
  d.onkeydown=e=>{if(e.key==='Tab'){p.hidden=true;return}e.preventDefault();
    if(['Enter',' ','ArrowDown'].includes(e.key)){e.stopPropagation();open()}else if(e.key==='Escape'&&!p.hidden){p.hidden=true;e.stopPropagation()}};
  p.onclick=e=>{e.preventDefault();const b=e.target.closest('button');if(!b||b.disabled)return;
    if(b.dataset.m){e.stopPropagation();v=new Date(v.getFullYear(),v.getMonth()+ +b.dataset.m,1);draw()}
    else{h.value=b.dataset.d;d.value=fmtDate(h.value);p.hidden=true;d.focus();h.dispatchEvent(new Event('input',{bubbles:true}))}};
  w.closest('form')?.addEventListener('reset',()=>{h.value=''});
}
function railNav(r,p,n){ /* prev/next arrows for a horizontally scrolling row */
  const upd=()=>{p.disabled=r.scrollLeft<4;n.disabled=r.scrollLeft+r.clientWidth>=r.scrollWidth-4};
  const go=d=>r.scrollBy({left:d*(r.firstElementChild.offsetWidth+20),behavior:'smooth'});
  p.onclick=()=>go(-1);n.onclick=()=>go(1);r.addEventListener('scroll',upd,{passive:true});addEventListener('resize',upd);upd();
}
document.addEventListener('submit',async e=>{
  const f=e.target.closest('form[data-api]');if(!f)return;e.preventDefault();
  const data=Object.fromEntries(new FormData(f)),b=$('button[type=submit]',f);b.disabled=true;
  try{const r=await api(f.dataset.api,{method:'POST',body:JSON.stringify(data)});
    toast(r.demo?'Demo mode: backend not connected':(f.dataset.ok||'Sent. Thank you!'));
    f.reset();f.dispatchEvent(new CustomEvent('saved',{bubbles:true,detail:{data,demo:!!r.demo}}));
  }catch{toast('Could not send. Check your details and try again.')}
  b.disabled=false;
});
document.addEventListener('DOMContentLoaded',()=>{
  const page=location.pathname.split('/').pop()||'index.html';
  const links=[['index.html','Home'],['menu.html','Shop'],['custom-cake.html','Custom Cakes'],['reviews.html','Reviews'],['contact.html','Contact']];
  const nav=()=>`<nav>${links.map(([h,t])=>`<a href="${h}"${h===page?' class="on"':''}>${t}</a>`).join('')}</nav>`;
  $('#site-header').innerHTML=`<header class="bar glass"><a class="logo" href="index.html">Crumb</a>${nav()}<div class="acts"><button id="mode" class="icon" aria-label="Toggle light or dark mode"></button><button id="cartb" class="icon" aria-label="Cart"><span data-i="bag"></span><span class="bd" id="cn" hidden></span></button><button id="acct" class="icon" data-i="user" aria-label="Account"></button><div id="pm" class="glass pm" hidden><div><b id="pn"></b><div class="mute" id="pe" style="font-size:.88rem"></div></div><a class="btn ghost sm" href="custom-cake.html">Book a cake</a><button id="out" class="btn sm">Sign out</button></div><button id="menu" class="icon menu" data-i="menu" aria-label="Menu"></button><a class="btn sm" href="menu.html">Order Now</a></div></header>`;
  $('#site-footer').innerHTML=`<footer class="wrap"><span>© ${new Date().getFullYear()} Crumb. Bakes, Curated.</span>${nav()}</footer>`;
  const b=$('#mode'),paint=()=>{b.dataset.i=root.dataset.theme==='dark'?'sun':'moon';icons()};
  b.onclick=()=>{root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';localStorage.setItem('crumb-theme',root.dataset.theme);paint()};
  $('#menu').onclick=()=>$('.bar').classList.toggle('open');
  paint();
  document.body.insertAdjacentHTML('beforeend',`<div id="auth" class="scrim" hidden><div class="glass modal" role="dialog" aria-modal="true" aria-label="Account"><button id="ax" class="icon x" data-i="x" aria-label="Close"></button>
<div><h3>Welcome to Crumb</h3><p class="mute" style="font-size:.92rem;margin-top:6px">Sign in to fill in your details faster when booking and reviewing.</p></div>
<div class="tabs"><button class="chip on" data-t="login">Sign in</button><button class="chip" data-t="register">Sign up</button></div>
<form id="fin" data-auth="/auth/login"><label>Email<input name="email" type="email" autocomplete="email" required></label><label>Password<input name="password" type="password" autocomplete="current-password" required></label><p class="err" role="alert"></p><button class="btn" type="submit">Sign in</button></form>
<form id="fup" data-auth="/auth/register" hidden><label>Full name<input name="name" autocomplete="name" required></label><label>Email<input name="email" type="email" autocomplete="email" required></label><label>Password<input name="password" type="password" minlength="8" autocomplete="new-password" required></label><label>Confirm password<input name="confirm" type="password" minlength="8" autocomplete="new-password" required></label><p class="err" role="alert"></p><button class="btn" type="submit">Create account</button></form></div></div>`);
  const A=$('#auth'),acct=$('#acct'),pm=$('#pm');
  const prefill=()=>{if(user)document.querySelectorAll('[data-api] [name=name],[data-api] [name=email],#co [name=name],#co [name=email]').forEach(el=>{if(!el.value)el.value=user[el.name]||''})};
  function paintUser(){
    acct.className='icon'+(user?' me':'');
    if(user){acct.removeAttribute('data-i');acct.textContent=String(user.name||user.email||'?')[0].toUpperCase();$('#pn').textContent=user.name||'Welcome';$('#pe').textContent=user.email||'';prefill()}
    else{acct.dataset.i='user';pm.hidden=true;icons()}
  }
  const save=u=>{user=u;u?localStorage.setItem('crumb-user',JSON.stringify(u)):localStorage.removeItem('crumb-user');paintUser()};
  const tab=t=>{A.querySelectorAll('.tabs .chip').forEach(c=>c.classList.toggle('on',c.dataset.t===t));$('#fin').hidden=t!=='login';$('#fup').hidden=t!=='register';A.querySelectorAll('.err').forEach(x=>x.textContent='')};
  const openA=(t='login')=>{tab(t);A.hidden=false;document.body.style.overflow='hidden';$('input',t==='login'?$('#fin'):$('#fup')).focus()};
  const closeA=()=>{A.hidden=true;document.body.style.overflow=''};
  acct.onclick=e=>{e.stopPropagation();user?pm.hidden=!pm.hidden:openA()};
  document.addEventListener('click',e=>{if(!pm.contains(e.target))pm.hidden=true});
  document.addEventListener('keydown',e=>{if(e.key==='Escape'){closeA();pm.hidden=true}});
  document.addEventListener('saved',prefill);
  A.onclick=e=>{if(e.target===A||e.target.closest('#ax'))closeA();const c=e.target.closest('.tabs .chip');if(c)tab(c.dataset.t)};
  $('#out').onclick=async()=>{try{await api('/auth/logout',{method:'POST'})}catch{}save(null);toast('Signed out')};
  A.addEventListener('submit',async e=>{
    const f=e.target;e.preventDefault();
    const d=Object.fromEntries(new FormData(f)),err=$('.err',f),b=$('button[type=submit]',f);err.textContent='';
    if('confirm' in d){if(d.password!==d.confirm){err.textContent='Passwords do not match.';return}delete d.confirm}
    b.disabled=true;
    try{const r=await api(f.dataset.auth,{method:'POST',body:JSON.stringify(d)});
      save({name:r.name||d.name||d.email.split('@')[0],email:r.email||d.email,token:r.token});
      f.reset();closeA();toast(f.id==='fup'?'Account created. Welcome to Crumb!':'Signed in. Welcome back!');
    }catch(x){err.textContent=x.status===401?'Incorrect email or password.':x.status===409?'An account with this email already exists.':(x.message&&isNaN(x.message)?x.message:'Something went wrong. Please try again.')}
    b.disabled=false;
  });
  if(!CONFIG.DEMO)api('/auth/me').then(u=>save({name:u.name,email:u.email,token:user?.token})).catch(()=>save(null));
  document.body.insertAdjacentHTML('beforeend',`<div id="cart" class="scrim side" hidden><aside class="glass drawer" role="dialog" aria-modal="true" aria-label="Cart"><div class="dh"><h3 id="ct">Your cart</h3><button id="cx" class="icon" data-i="x" aria-label="Close cart"></button></div>
<div id="cv"><div id="ci"></div><div class="foot"><div class="tot"><span class="mute">Subtotal</span><b id="sub"></b></div><button id="chk" class="btn">Checkout</button></div></div>
<form id="co" hidden><div class="g2"><label>Full name<input name="name" autocomplete="name" required></label><label>Phone<input name="phone" type="tel" autocomplete="tel" required></label></div><label>Email<input name="email" type="email" autocomplete="email" required></label>
<div class="g2"><label>Receive by<select name="fulfilment"><option>Pickup</option><option>Delivery</option></select></label><label>Needed on<input name="date" type="date" required></label></div>
<label id="adr" hidden>Delivery address<textarea name="address" style="min-height:80px"></textarea></label><label>Notes (optional)<input name="notes"></label>
<div class="tot"><span class="mute">Total</span><b id="tt"></b></div><button class="btn" type="submit">Place order</button><button class="btn ghost" type="button" id="cb">Back to cart</button></form></aside></div>`);
  const C=$('#cart'),showCo=b=>{$('#cv').hidden=b;$('#co').hidden=!b;$('#ct').textContent=b?'Checkout':'Your cart'};
  const openC=()=>{showCo(false);C.hidden=false;document.body.style.overflow='hidden'},closeC=()=>{C.hidden=true;document.body.style.overflow=''};
  $('#cartb').onclick=openC;$('#chk').onclick=()=>{showCo(true);prefill()};$('#cb').onclick=()=>showCo(false);
  C.onclick=e=>{if(e.target===C||e.target.closest('#cx'))closeC()};
  document.addEventListener('keydown',e=>{if(e.key==='Escape')closeC()});
  $('#co [name=date]').min=new Date().toISOString().slice(0,10);
  $('#co [name=fulfilment]').onchange=e=>{const d=e.target.value==='Delivery';$('#adr').hidden=!d;$('#adr textarea').required=d};
  $('#co').addEventListener('submit',async e=>{
    e.preventDefault();const f=e.target,d=Object.fromEntries(new FormData(f)),b=$('button[type=submit]',f);b.disabled=true;
    try{const r=await api('/orders',{method:'POST',body:JSON.stringify({...d,items:cart.map(i=>({productId:i.id,quantity:i.qty}))})});
      toast(r.demo?'Demo mode: order not sent, backend not connected':'Order placed. Thank you!');
      cart=[];cartSave();f.reset();$('#adr').hidden=true;closeC();prefill();
    }catch{toast('Could not place the order. Please try again.')}
    b.disabled=false;
  });
  paintCart();
  paintUser();
  document.querySelectorAll('select').forEach(enhanceSelect);document.querySelectorAll('input[type=date]').forEach(enhanceDate);
  icons();
});
