$ErrorActionPreference = 'Stop'

$polytoneUrl = 'https://cdn.modrinth.com/data/3qAYkBMB/versions/q9pM3nyE/polytone-26.2-6.8.2-fabric.jar'
$polytoneSha512 = 'ae5e3f6c6c4478bdf357ea35e44d397deaea2d733a6277dc6277e8251347be9a82e2fb5809341a04393cafc731b2b8d15b9c762bfeb70474ec40e8cd401ab51f'
$nautilusUrl = 'https://cdn.modrinth.com/data/zRIA8Bvc/versions/dEFwwlqx/nautilus_studio-26.2-1.11.0-fabric.jar'
$nautilusSha512 = '3888d011c95c5eac5b1d7ff9aa4f8b1a67e1507f74749f328eb92349fe45b97cd6f533e8a824cfd981679e309479552c56e3325348c96d540ff71d2a5f6b1db0'

function Download-Verified($url, $sha512, $path) {
    Invoke-WebRequest -Uri $url -OutFile $path
    $actual = (Get-FileHash -Path $path -Algorithm SHA512).Hash.ToLowerInvariant()
    if ($actual -ne $sha512) { throw "SHA-512 mismatch for $path" }
}

New-Item -ItemType Directory -Force 'common/mods', 'fabric/mods', 'work/vendor' | Out-Null
Download-Verified $polytoneUrl $polytoneSha512 'work/vendor/polytone-26.2.jar'
Download-Verified $nautilusUrl $nautilusSha512 'work/vendor/nautilus_studio-fabric-26.2-1.11.0.jar'

Push-Location 'work/vendor'
try {
    jar xf 'polytone-26.2.jar' 'META-INF/jars/codecui-fabric-26.2-1.4.5.jar' 'META-INF/jars/nexp-1.2.1.jar'
} finally {
    Pop-Location
}

Copy-Item 'work/vendor/META-INF/jars/codecui-fabric-26.2-1.4.5.jar' 'common/mods/codecui-common-26.2-1.4.5.jar'
Copy-Item 'work/vendor/META-INF/jars/codecui-fabric-26.2-1.4.5.jar' 'fabric/mods/codecui-fabric-26.2-1.4.5.jar'
Copy-Item 'work/vendor/nautilus_studio-fabric-26.2-1.11.0.jar' 'common/mods/nautilus_studio-common-26.2-1.11.0.jar'
Copy-Item 'work/vendor/nautilus_studio-fabric-26.2-1.11.0.jar' 'fabric/mods/nautilus_studio-fabric-26.2-1.11.0.jar'
Copy-Item 'work/vendor/META-INF/jars/nexp-1.2.1.jar' 'common/mods/nexp-1.2.1.jar'
Copy-Item 'work/vendor/META-INF/jars/nexp-1.2.1.jar' 'fabric/mods/nexp-1.2.1.jar'
