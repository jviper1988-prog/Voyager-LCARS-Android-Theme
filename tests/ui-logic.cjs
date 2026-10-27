const fs=require('fs'),vm=require('vm'),assert=require('assert');
const file=require('path').join(__dirname,'../app/src/main/assets/index.html');
const html=fs.readFileSync(file,'utf8'),script=html.match(/<script>([\s\S]*?)<\/script>/)[1];
function boot(seed={}){
 let time=1000000,fail=false,next=1;const intervals=new Map(),data={...seed},els=new Map(),listeners={};
 class El{constructor(id=''){this.id=id;this.value='';this.textContent='';this.style={};this.dataset={};this.children=[];this.events={};this.classList={add(){},remove(){},toggle(){}}}setAttribute(){}addEventListener(n,f){(this.events[n]??=[]).push(f)}focus(){this.focused=true}setSelectionRange(a,b){this.selectionStart=a;this.selectionEnd=b}append(...a){this.children.push(...a)}appendChild(a){this.append(a)}replaceChildren(){this.children=[]}get childElementCount(){return this.children.length}click(){for(const f of this.events.click||[])f({})}}
 const el=id=>{if(!els.has(id))els.set(id,new El(id));return els.get(id)};
 class Clock extends Date{constructor(...args){super(...(args.length?args:[time]))}static now(){return time}}
 const document={getElementById:el,querySelectorAll:()=>[],createElement:()=>new El(),createTextNode:t=>t,addEventListener:(n,f)=>listeners[n]=f,hidden:false};
 let starts=0,stops=0;const media=[];
 class Audio {constructor(src){this.src=src;this.currentTime=0;this.paused=true;media.push(this)}play(){starts++;this.paused=false;return Promise.resolve()}pause(){stops++;this.paused=true}}
 const window={addEventListener(){},scrollTo(){},location:{}};
 const ctx={Audio,window,document,navigator:{onLine:true},localStorage:{getItem:k=>data[k]??null,setItem:(k,v)=>{if(fail)throw Error('quota');data[k]=v}},Date:Clock,setInterval:f=>{let id=next++;intervals.set(id,f);return id},clearInterval:id=>intervals.delete(id),setTimeout:()=>1,clearTimeout(){},confirm:()=>true,Blob,URL};
 vm.runInNewContext(script,ctx);
 return {el,data,document,media,audio:()=>({starts,stops}),setFail:v=>fail=v,advance:ms=>{time+=ms;for(const f of [...intervals.values()])f()},resume:()=>listeners.visibilitychange(),window};
}
let passed=0;function test(name,f){f();console.log('PASS '+name);passed++}
test('calculator precedence and parentheses',()=>{const t=boot();t.el('mathInput').value='(7+3)*42';t.el('calculate').click();assert.equal(t.el('mathResult').textContent,'420')});
test('calculator rejects division by zero and excessive length',()=>{const t=boot();for(const value of ['1/0','('.repeat(300)]){t.el('mathInput').value=value;t.el('calculate').click();assert.equal(t.el('mathResult').textContent,'ERROR')}});
test('failed log save preserves draft and storage',()=>{const t=boot();t.setFail(true);t.el('logEntry').value='Keep my draft';t.el('saveLog').click();assert.equal(t.el('logEntry').value,'Keep my draft');assert.equal(t.data['voyager.logs'],undefined);assert.match(t.el('toast').textContent,/Unable to save/)});
test('successful log save and failed clear preserve saved entry',()=>{const t=boot();t.el('logEntry').value='Captain entry';t.el('saveLog').click();assert.equal(t.el('logEntry').value,'');const saved=t.data['voyager.logs'];assert.equal(JSON.parse(saved)[0].text,'Captain entry');t.setFail(true);t.el('clearLogs').click();assert.equal(t.data['voyager.logs'],saved)});
test('malformed saved log records do not crash startup',()=>{boot({'voyager.logs':'[null,{}, {"text":2,"date":"x"}]'})});
test('timer catches up after delayed callback',()=>{const t=boot();t.el('timerStart').click();t.advance(65000);assert.equal(t.el('timerDisplay').textContent,'03:55');t.advance(235000);assert.equal(t.el('timerDisplay').textContent,'00:00');assert.equal(t.el('timerStart').textContent,'START')});
test('paused timer stays paused; extra minute and reset work',()=>{const t=boot();t.el('timerStart').click();t.advance(30000);t.el('timerStart').click();t.advance(60000);assert.equal(t.el('timerDisplay').textContent,'04:30');t.el('timerPlus').click();assert.equal(t.el('timerDisplay').textContent,'05:30');t.el('timerReset').click();assert.equal(t.el('timerDisplay').textContent,'05:00')});
test('favorite storage failure does not pin shortcut',()=>{const t=boot();t.setFail(true);t.el('appList').children[0].children[1].click();assert.equal(t.el('favoriteList').childElementCount,0);t.setFail(false);t.el('appList').children[0].children[1].click();assert.equal(t.el('favoriteList').childElementCount,1)});
test('audio and haptic settings persist',()=>{const t=boot();t.el('soundToggle').click();t.el('hapticToggle').click();assert.equal(t.data['voyager.sound'],'1');assert.equal(t.data['voyager.haptic'],'1')});
test('built-in keypad enters operators, edits, and computes',()=>{const t=boot();const press=k=>t.el('mathKeys').children.find(b=>b.textContent===k).click();for(const k of ['(','7','+','3',')','×','4','2','='])press(k);assert.equal(t.el('mathResult').textContent,'420');press('⌫');assert.equal(t.el('mathInput').value,'(7+3)*4');press('C');assert.equal(t.el('mathInput').value,'')});
test('keyboard button focuses equation input',()=>{const t=boot();t.el('mathKeyboard').click();assert.equal(t.el('mathInput').focused,true)});
test('red alert sounds with touch sounds off and stops when deactivated',()=>{const t=boot();t.el('alertToggle').click();assert.ok(t.audio().starts>0);assert.equal(t.media[2].src,'audio/red-alert.mp3');assert.equal(t.media[2].loop,true);const n=t.audio().starts;t.el('alertToggle').click();t.advance(3000);assert.equal(t.audio().starts,n)});
test('alarm mute persists and prevents alert audio',()=>{const t=boot();t.el('alarmToggle').click();t.el('alertToggle').click();assert.equal(t.audio().starts,0);assert.equal(t.data['voyager.alarm'],'0')});
test('alarm stops when native activity pauses',()=>{const t=boot();t.el('alertToggle').click();const n=t.audio().starts;t.window.LCARS.pause();assert.equal(t.media[2].paused,true);t.advance(3000);assert.equal(t.audio().starts,n)});
console.log(`${passed} simulated UI logic checks passed. Not browser rendering or Android hardware tests.`);
