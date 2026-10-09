/* Theme applies before first paint. Only the colour preference is stored. */
(()=>{
  const root=document.documentElement;
  const preference=window.matchMedia('(prefers-color-scheme: dark)');
  let saved=null;
  try{saved=localStorage.getItem('crumb-theme')}catch{}
  if(!['light','dark'].includes(saved))saved=null;
  function apply(theme){
    root.dataset.theme=theme;
    const button=document.getElementById('theme-toggle');
    if(button){button.setAttribute('aria-checked',String(theme==='dark'));button.title=theme==='dark'?'Switch to light mode':'Switch to dark mode'}
  }
  apply(saved||(preference.matches?'dark':'light'));
  preference.addEventListener('change',event=>{if(!saved)apply(event.matches?'dark':'light')});
  window.addEventListener('storage',event=>{if(event.key==='crumb-theme'){saved=['light','dark'].includes(event.newValue)?event.newValue:null;apply(saved||(preference.matches?'dark':'light'))}});
  document.addEventListener('DOMContentLoaded',()=>{
    apply(root.dataset.theme);
    document.getElementById('theme-toggle').addEventListener('click',()=>{
      saved=root.dataset.theme==='dark'?'light':'dark';apply(saved);
      try{localStorage.setItem('crumb-theme',saved)}catch{}
    });
    const nav=document.querySelector('header nav');
    const indicator=nav.querySelector('.nav-indicator');
    let positioned=false;
    const placeIndicator=(target)=>{
      if(!target){indicator.style.opacity='0';return}
      const tab=target.getBoundingClientRect(),bounds=nav.getBoundingClientRect();
      // One DOM node moves between tabs. left/width are transitioned in CSS.
      indicator.style.left=(tab.left-bounds.left+nav.scrollLeft)+'px';
      indicator.style.width=tab.width+'px';
      indicator.style.opacity='1';
      if(!positioned){positioned=true;requestAnimationFrame(()=>nav.classList.add('indicator-ready'))}
    };
    window.syncNavIndicator=()=>placeIndicator(nav.querySelector('a.active'));
    // Start the movement on the click itself, before the hashchange rerenders the page.
    nav.addEventListener('click',event=>{
      const target=event.target.closest('a');
      if(!target||!nav.contains(target)||event.button===1||event.metaKey||event.ctrlKey||event.shiftKey||event.altKey)return;
      placeIndicator(target);
    });
    window.syncNavIndicator();
    if('ResizeObserver' in window)new ResizeObserver(window.syncNavIndicator).observe(nav);
    else window.addEventListener('resize',window.syncNavIndicator);
    document.fonts?.ready.then(()=>window.syncNavIndicator());
    const reduced=window.matchMedia('(prefers-reduced-motion: reduce)');
    const backToTop=document.getElementById('back-to-top');
    if(backToTop){
      const syncBackToTop=()=>{
        const visible=window.scrollY>420;
        backToTop.classList.toggle('is-visible',visible);
        backToTop.setAttribute('aria-hidden',String(!visible));
        backToTop.tabIndex=visible?0:-1;
      };
      window.addEventListener('scroll',syncBackToTop,{passive:true});
      backToTop.addEventListener('click',()=>window.scrollTo({top:0,left:0,behavior:reduced.matches?'auto':'smooth'}));
      syncBackToTop();
    }
    document.addEventListener('click',event=>{
      const button=event.target.closest('.btn');
      if(!button||button.disabled||reduced.matches)return;
      const rect=button.getBoundingClientRect(),size=Math.max(rect.width,rect.height),wave=document.createElement('span');
      wave.className='tap-wave';wave.setAttribute('aria-hidden','true');
      Object.assign(wave.style,{width:size+'px',height:size+'px',left:((event.detail?event.clientX-rect.left:rect.width/2)-size/2)+'px',top:((event.detail?event.clientY-rect.top:rect.height/2)-size/2)+'px'});
      button.append(wave);wave.addEventListener('animationend',()=>wave.remove(),{once:true});
    });
    const count=document.getElementById('cart-count');
    new MutationObserver(()=>{if(reduced.matches)return;count.classList.remove('bag-pop');void count.offsetWidth;count.classList.add('bag-pop')}).observe(count,{childList:true});

  });
})();
