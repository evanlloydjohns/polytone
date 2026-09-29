"""Retarget the two moved RegistryCodecs calls in the published 26.2 CodecUI.

The original JAR is downloaded and SHA-512 verified by bootstrap-26.2-deps.ps1.
This narrow class-file patch is temporary until CodecUI publishes a 26.3 build.
"""

from pathlib import Path
import struct
import sys
import zipfile

OLD_OWNER = b"net/minecraft/core/RegistryCodecs"
NEW_OWNER = b"net/minecraft/core/registries/codec/RegistryCodecs"
OLD_METHOD = b"homogeneousList"
NEW_METHOD = b"holderSet"
TARGET = "net/mehvahdjukaar/codecui/SchemaCodecs.class"


def retarget_class(data: bytes) -> bytes:
    if data[:4] != b"\xca\xfe\xba\xbe":
        raise ValueError("Not a Java class file")
    count = struct.unpack_from(">H", data, 8)[0]
    out = bytearray(data[:10])
    offset = 10
    index = 1
    replacements = [0, 0]
    while index < count:
        tag = data[offset]
        out.append(tag)
        offset += 1
        if tag == 1:  # CONSTANT_Utf8
            length = struct.unpack_from(">H", data, offset)[0]
            value = data[offset + 2:offset + 2 + length]
            for slot, old, new in ((0, OLD_OWNER, NEW_OWNER),
                                   (1, OLD_METHOD, NEW_METHOD)):
                replacements[slot] += value.count(old)
                value = value.replace(old, new)
            out += struct.pack(">H", len(value)) + value
            offset += 2 + length
        else:
            size = {3: 4, 4: 4, 5: 8, 6: 8, 7: 2, 8: 2,
                    9: 4, 10: 4, 11: 4, 12: 4, 15: 3,
                    16: 2, 17: 4, 18: 4, 19: 2, 20: 2}.get(tag)
            if size is None:
                raise ValueError(f"Unknown constant-pool tag {tag}")
            out += data[offset:offset + size]
            offset += size
            if tag in (5, 6):
                index += 1
        index += 1
    if replacements != [1, 1]:
        raise ValueError(f"Unexpected CodecUI bytecode shape: {replacements}")
    out += data[offset:]
    return bytes(out)


def main(path: Path) -> None:
    temp = path.with_suffix(".patched.jar")
    try:
        with zipfile.ZipFile(path) as source, zipfile.ZipFile(temp, "w") as target:
            if TARGET not in source.namelist():
                raise ValueError(f"Missing {TARGET}")
            for entry in source.infolist():
                data = source.read(entry)
                if entry.filename == TARGET:
                    data = retarget_class(data)
                target.writestr(entry, data)
        with zipfile.ZipFile(temp) as check:
            if check.testzip() is not None:
                raise ValueError("Patched CodecUI archive failed ZIP validation")
        temp.replace(path)
    finally:
        temp.unlink(missing_ok=True)


if __name__ == "__main__":
    main(Path(sys.argv[1]))
