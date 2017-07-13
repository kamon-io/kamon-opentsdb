package ru.mfms

package object kamon {
    implicit class StrOpt(string: String) {
        def normalize = string.replace(".", "_").replace(":", "_")
    }
}
