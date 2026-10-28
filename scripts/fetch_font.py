"""Download unmodified Norse Bold from the designer for permitted app embedding."""
import hashlib, io, urllib.request, zipfile
from pathlib import Path
url="https://www.joelcarrouche.com/media/pages/fonts/norse/5df431c716-1675783618/norse.zip"
data=urllib.request.urlopen(url,timeout=60).read()
expected="e2b53fedcdc5942320fbe776c7fd2c1c70172740ee86c216b8b6071f0bc4b8e4"
if hashlib.sha256(data).hexdigest()!=expected:
    raise SystemExit("Font archive changed: review its license and contents before updating the checksum")
root=Path(__file__).resolve().parents[1]/"app/src/main/assets"
with zipfile.ZipFile(io.BytesIO(data)) as z:
    for filename,destination in [("Norsebold.otf","fonts/Norsebold.otf"),("freefont_license.txt","licenses/Norse-license.txt")]:
        candidates=[n for n in z.namelist() if Path(n).name==filename and not n.startswith("__MACOSX/")]
        if len(candidates)!=1: raise SystemExit("Unexpected font archive contents")
        target=root/destination
        target.parent.mkdir(parents=True,exist_ok=True)
        target.write_bytes(z.read(candidates[0]))
print("Norse Bold and designer license prepared for app embedding")
