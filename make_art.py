from PIL import Image,ImageDraw,ImageFont,ImageFilter
from pathlib import Path
p=Path(__file__).parent
W,H=1440,3120
ink=(0,0,0); peach=(255,182,110);salmon=(242,157,137); violet=(145,136,199);orchid=(192,155,206);blue=(144,188,227)
font='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
bold='/usr/share/fonts/truetype/dejavu/DejaVuSansCondensed-Bold.ttf'
def F(s,b=False):return ImageFont.truetype(bold if b else font,s)
im=Image.new('RGB',(W,H),ink);d=ImageDraw.Draw(im)
# left period-correct LCARS structural bands and button blocks
d.rounded_rectangle((34,98,284,2820),radius=110,fill=peach)
d.rectangle((135,234,W-38,348),fill=peach)
d.rectangle((220,235,500,2810),fill=ink)
# make slender cap rails to avoid overpowering central dark wallpaper
d.rounded_rectangle((75,500,181,920),40,fill=orchid)
d.rounded_rectangle((75,936,181,1320),40,fill=salmon)
d.rounded_rectangle((75,1340,181,1744),40,fill=violet)
d.rounded_rectangle((75,1770,181,2140),40,fill=blue)
d.rounded_rectangle((75,2160,181,2550),40,fill=peach)
d.rounded_rectangle((74,2804,290,2938),45,fill=salmon)
# top title
for x,y,txt,sz,fill in [(375,130,'U.S.S. VOYAGER',76,peach),(377,232,'NCC-74656  •  LCARS 2378',34,ink),(360,730,'FEDERATION',53,orchid),(360,798,'STARSHIP SYSTEMS',42,orchid),(358,2125,'THE JOURNEY',58,peach),(358,2200,'CONTINUES',58,peach),(357,2760,'TACTICAL DISPLAY // 01',28,blue)]:
 d.text((x,y),txt,font=F(sz,True),fill=fill)
# intersecting detail bands
d.rounded_rectangle((358,950,1335,967),8,fill=salmon)
d.rounded_rectangle((358,995,1120,1009),6,fill=violet)
d.rounded_rectangle((358,1035,790,1048),6,fill=blue)
# concentric eliptical orbital path rings, LCARS graphic
d.ellipse((410,1260,1260,2110),outline=orchid,width=16)
d.arc((492,1343,1182,2033),25,285,fill=peach,width=33)
d.arc((555,1406,1120,1970),170,430,fill=violet,width=28)
d.ellipse((767,1630,907,1770),fill=salmon)
d.line((835,1250,835,2113),fill=orchid,width=9)
d.line((412,1702,1260,1702),fill=blue,width=9)
d.rounded_rectangle((358,2520,1330,2560),12,fill=violet)
d.rounded_rectangle((358,2585,1080,2605),9,fill=salmon)
im.save(p/'Voyager_LCARS_Wallpaper.png',optimize=True)
# draw app icon
for size in (512,192,144):
 icon=Image.new('RGBA',(size,size),(0,0,0,255));i=ImageDraw.Draw(icon)
 x=lambda n:round(n*size/512)
 i.rounded_rectangle((x(24),x(24),x(488),x(488)),radius=x(99),fill=(0,0,0,255),outline=peach,width=x(16))
 i.rounded_rectangle((x(75),x(78),x(167),x(426)),radius=x(46),fill=orchid)
 i.rounded_rectangle((x(190),x(78),x(433),x(163)),radius=x(39),fill=salmon)
 i.rounded_rectangle((x(190),x(185),x(433),x(270)),radius=x(30),fill=violet)
 i.rounded_rectangle((x(190),x(295),x(433),x(380)),radius=x(30),fill=peach)
 i.text((x(220),x(392)),'LCARS',font=F(x(35),True),fill=peach)
 if size==512:icon.save(p/'Voyager_LCARS_App_Icon.png')
 if size==192:icon.save(p/'app/src/main/res/drawable/ic_launcher.png')
 if size==144:icon.save(p/'preview/Icon_144.png')
print('art saved')
