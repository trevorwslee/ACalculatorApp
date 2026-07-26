set -ex
wasm-pack build --target web


rm -rf ../app/src/main/assets/bridge
mkdir ../app/src/main/assets/bridge
cp *.html ../app/src/main/assets/bridge/
cp -r pkg ../app/src/main/assets/bridge/pkg