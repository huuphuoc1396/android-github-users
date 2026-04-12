#include <jni.h>
#include <string>
#include "credentials.cpp"

extern "C" jstring
Java_com_example_github_users_core_security_AppSecretsImpl_getSecretKey(
        JNIEnv *env,
        jclass,
        jint id
) {
    return env->NewStringUTF(getSecretKey(id).c_str());
}
