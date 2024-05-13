set -ex
wasm-pack build --target web


rm -rf ../app/src/main/assets/bridge
mkdir ../app/src/main/assets/bridge
cp index.html ../app/src/main/assets/bridge/index.html
cp -r pkg ../app/src/main/assets/bridge/pkg