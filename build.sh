#!/bin/bash
chmod 755 ./gradlew
function makeDirectory(){
  if [ ! -d $1 ]; then
    mkdir $1
  fi
}
makeDirectory ./exec
makeDirectory ./exec/tmp
makeDirectory ./exec/Linux
makeDirectory ./exec/MacOS
makeDirectory ./exec/Windows
makeDirectory ./exec/tmp/Carbon-Linux-x86_64
makeDirectory ./exec/tmp/Carbon-Linux-i386
makeDirectory ./exec/tmp/Carbon-Windows-x86_64
makeDirectory ./exec/tmp/Carbon-Windows-i386
makeDirectory ./exec/tmp/Carbon-MacOS-x86_64
./gradlew shadowJar
cp ./build/libs/Carbon.jar ./exec/tmp/Carbon-Linux-x86_64
cp ./build/libs/Carbon.jar ./exec/tmp/Carbon-Linux-i386
cp ./build/libs/Carbon.jar ./exec/tmp/Carbon-Windows-x86_64
cp ./build/libs/Carbon.jar ./exec/tmp/Carbon-Windows-i386
cp ./build/libs/Carbon.jar ./exec/tmp/Carbon-MacOS-x86_64
gcc ./Carbon-Launcher/linux.c -O3 -o ./exec/tmp/Carbon-Linux-x86_64/Carbon-Linux-x86_64
gcc ./Carbon-Launcher/linux.c -m32 -O3 -o ./exec/tmp/Carbon-Linux-i386/Carbon-Linux-i386
x86_64-w64-mingw32-windres -i ./Carbon-Launcher/img.rc --input-format=rc -o ./exec/tmp/img64.res -O coff
i686-w64-mingw32-windres -i ./Carbon-Launcher/img.rc --input-format=rc -o ./exec/tmp/img32.res -O coff
x86_64-w64-mingw32-gcc ./Carbon-Launcher/windows.c -c -O3 -o ./exec/tmp/Carbon-Windows-x86_64.o
i686-w64-mingw32-gcc ./Carbon-Launcher/windows.c -c -O3 -o ./exec/tmp/Carbon-Windows-i386.o
x86_64-w64-mingw32-gcc ./exec/tmp/Carbon-Windows-x86_64.o ./exec/tmp/img64.res -o ./exec/tmp/Carbon-Windows-x86_64/Carbon-Windows-x86_64.exe
i686-w64-mingw32-gcc ./exec/tmp/Carbon-Windows-i386.o ./exec/tmp/img32.res -o ./exec/tmp/Carbon-Windows-i386/Carbon-Windows-i386.exe
x86_64-apple-darwin14-clang ./Carbon-Launcher/macos.c -o ./exec/tmp/Carbon-MacOS-x86_64/Carbon-MacOS-x86_64
cd ./exec/tmp
tar -zcvf ../Linux/Carbon-Linux-x86_64.tar.gz ./Carbon-Linux-x86_64
tar -zcvf ../Linux/Carbon-Linux-i386.tar.gz ./Carbon-Linux-i386
tar -zcvf ../MacOS/Carbon-MacOS-x86_64.tar.gz ./Carbon-MacOS-x86_64
zip -r -q -o ../Windows/Carbon-Windows-x86_64.zip ./Carbon-Windows-x86_64
zip -r -q -o ../Windows/Carbon-Windows-i386.zip ./Carbon-Windows-i386
cd ../
rm -rf ./tmp
cd ../
./gradlew clean