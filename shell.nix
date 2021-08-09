with import <nixpkgs> {};

stdenv.mkDerivation {
  name = "arend-lib-env";
  buildInputs = [
    pkgs.jdk11
  ];
}
