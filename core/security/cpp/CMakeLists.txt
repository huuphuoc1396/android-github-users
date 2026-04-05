cmake_minimum_required(VERSION 3.22.1)

add_library(
        secret-keys-lib
        SHARED
        libsecret-keys-lib.cpp
)

target_link_options(secret-keys-lib PRIVATE "-Wl,-z,max-page-size=16384")
