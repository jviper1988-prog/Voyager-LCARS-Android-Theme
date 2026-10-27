import cairosvg
from pathlib import Path
p=Path(__file__).parent
svg='''<svg xmlns="http://www.w3.org/2000/svg" width="520" height="1120" viewBox="0 0 520 1120">
<rect width="520" height="1120" fill="#000000"/>
<path fill="#ffb66e" d="M24 27 H141 V118 H76 V84 Q76 66 95 66 H141 V27 Z"/>
<rect x="139" y="27" width="353" height="27" rx="13" fill="#ffb66e"/>
<text x="154" y="82" font-size="15" fill="#ffb66e" font-family="sans-serif" font-weight="bold">NCC-74656 / LCARS 2378</text><text x="153" y="123" font-size="31" fill="#f0e7da" font-family="sans-serif">U.S.S. VOYAGER</text>
<rect x="20" y="133" width="117" height="72" rx="12" fill="#ffb66e"/><text x="28" y="167" font-size="13" font-family="sans-serif" font-weight="bold">SEC. 001</text><text x="28" y="185" font-size="11" font-family="sans-serif">MAIN CONTROL</text>
<rect x="20" y="212" width="117" height="78" rx="12" fill="#f29d89"/><text x="39" y="252" font-size="20" font-family="sans-serif" font-weight="bold">HOME</text><text x="38" y="267" font-size="9" font-family="sans-serif">01 / BRIDGE</text>
<rect x="20" y="297" width="117" height="78" rx="12" fill="#c09bce"/><text x="47" y="339" font-size="20" font-family="sans-serif" font-weight="bold">APPS</text><text x="37" y="354" font-size="9" font-family="sans-serif">02 / ACCESS</text>
<rect x="20" y="382" width="117" height="78" rx="12" fill="#9188c7"/><text x="40" y="422" font-size="18" font-family="sans-serif" font-weight="bold">TOOLS</text><text x="35" y="439" font-size="9" font-family="sans-serif">03 / SYSTEMS</text>
<rect x="20" y="467" width="117" height="78" rx="12" fill="#90bce3"/><text x="45" y="510" font-size="20" font-family="sans-serif" font-weight="bold">LOGS</text><text x="31" y="525" font-size="9" font-family="sans-serif">04 / CAPTAIN</text>
<rect x="20" y="552" width="117" height="78" rx="12" fill="#ffb66e"/><text x="32" y="593" font-size="18" font-family="sans-serif" font-weight="bold">CONFIG</text><text x="43" y="609" font-size="9" font-family="sans-serif">05 / SHIP</text>
<rect x="20" y="637" width="117" height="355" rx="12" fill="#c09bce"/><text x="54" y="925" font-size="15" fill="black" font-family="sans-serif" font-weight="bold">LCARS</text><text x="57" y="946" font-size="16" fill="black" font-family="sans-serif">74656</text>
<rect x="20" y="1000" width="117" height="63" rx="10" fill="#f29d89"/><text x="31" y="1034" font-size="16" font-family="sans-serif" font-weight="bold">VOYAGER</text>
<rect x="148" y="147" width="60" height="12" rx="6" fill="#f29d89"/><rect x="216" y="147" width="135" height="12" rx="6" fill="#c09bce"/><rect x="359" y="147" width="49" height="12" rx="6" fill="#90bce3"/><rect x="416" y="147" width="76" height="12" rx="6" fill="#ffb66e"/>
<text x="152" y="194" font-size="13" fill="#ffb66e" font-family="sans-serif" font-weight="bold">BRIDGE / OPERATIONS // SYSTEM ONLINE</text>
<text x="150" y="239" font-size="37" fill="#f0e7da" font-family="sans-serif">COMMAND</text><text x="150" y="281" font-size="37" fill="#f0e7da" font-family="sans-serif">INTERFACE</text>
<rect x="149" y="309" width="195" height="122" rx="12" fill="#1a1520" stroke="#f29d89" stroke-width="3"/><text x="164" y="331" fill="#9f9baf" font-size="10" font-family="sans-serif">SHIPBOARD CHRONOMETER</text><text x="159" y="386" fill="#ffd1a9" font-size="51" font-family="sans-serif">20:24</text><text x="162" y="411" fill="#90bce3" font-size="12" font-family="sans-serif">SAT OCT 24, 2026</text>
<rect x="355" y="309" width="137" height="122" rx="12" fill="#1a1520" stroke="#c09bce" stroke-width="3"/><text x="367" y="331" fill="#9f9baf" font-size="9" font-family="sans-serif">ENERGY RESERVES</text><text x="369" y="386" fill="#ffd1a9" font-size="43" font-family="sans-serif">88%</text><rect x="369" y="398" width="105" height="10" rx="4" fill="#514052"/><rect x="369" y="398" width="91" height="10" rx="4" fill="#f29d89"/>
<text x="150" y="463" fill="#c09bce" font-size="15" font-family="sans-serif" font-weight="bold">PRIMARY ACCESS</text><rect x="296" y="451" width="194" height="6" rx="3" fill="#c09bce"/>
'''
buttons=[('COMMUNICATIONS','DIALER','#ffb66e'),('SUBSPACE MSG','MESSAGING','#c09bce'),('SCIENCE','WEB BROWSER','#f29d89'),('NAVIGATION','MAPS / ROUTING','#90bce3'),('VISUAL SENSOR','CAMERA','#ffb66e'),('ENGINEERING','SETTINGS','#c09bce')]
for ix,(a,b,c) in enumerate(buttons):
 x=149+(ix%2)*174;y=475+(ix//2)*124
 svg+=f'<rect x="{x}" y="{y}" width="165" height="114" rx="15" fill="{c}"/><text x="{x+12}" y="{y+50}" font-family="sans-serif" font-size="14" font-weight="bold" fill="black">{a}</text><text x="{x+12}" y="{y+80}" font-family="sans-serif" font-size="10" fill="#2b2333">{b}</text>'
svg+='''<rect x="149" y="865" width="108" height="90" rx="8" fill="#1b1722" stroke="#9188c7"/><rect x="266" y="865" width="108" height="90" rx="8" fill="#1b1722" stroke="#9188c7"/><rect x="383" y="865" width="109" height="90" rx="8" fill="#1b1722" stroke="#9188c7"/><text x="159" y="890" fill="#90bce3" font-size="11" font-family="sans-serif">STARBASE LINK</text><text x="159" y="925" fill="white" font-size="15" font-family="sans-serif">CONNECTED</text><text x="279" y="890" fill="#90bce3" font-size="11" font-family="sans-serif">DATA FEED</text><text x="279" y="925" fill="white" font-size="15" font-family="sans-serif">ANDROID</text><text x="393" y="890" fill="#90bce3" font-size="11" font-family="sans-serif">SHIP STATUS</text><text x="393" y="925" fill="white" font-size="15" font-family="sans-serif">NOMINAL</text>
<rect x="20" y="1078" width="117" height="20" rx="6" fill="#ffb66e"/><rect x="148" y="1078" width="344" height="11" rx="6" fill="#ffb66e"/><text x="149" y="1109" font-size="9" fill="#9f9baf" font-family="sans-serif">FEDERATION STARSHIP SYSTEMS</text><text x="380" y="1109" font-size="9" fill="#ffb66e" font-family="sans-serif">VOY-2378 / LCARS</text></svg>'''
cairosvg.svg2png(bytestring=svg.encode(),write_to=str(p/'Voyager_LCARS_Design_Preview.png'),output_width=520,output_height=1120)
print('generated')
