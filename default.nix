{ pkgs ? import <nixpkgs> {} }:
let
  stdenv = pkgs.stdenv;
  jdk = pkgs.openjdk8;
in stdenv.mkDerivation rec {
  name = "defbot";
  version = "1.0";
  buildInputs = [
    jdk
    (pkgs.leiningen.override {
      jdk = jdk;
    })
  ];

  java = "${jdk}/bin/java";
  
  src = ./.;

  preFixup = ''
    substituteAllInPlace $out/bin/defbot
  '';
  
}

