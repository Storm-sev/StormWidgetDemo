package com.example.customnewdemo.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.UnsupportedOperationException

class ShellUtils private constructor() {
    /**
     * The result of command.
     */
    class CommandResult(var result: Int, var successMsg: String, var errorMsg: String) {
        override fun toString(): String {
            return """
                 result: $result
                 successMsg: $successMsg
                 errorMsg: $errorMsg
                 """.trimIndent()
        }
    }

    companion object {
        private val LINE_SEP = System.getProperty("line.separator")

        /**
         * Execute the command.
         *
         * @param command  The command.
         * @param isRooted True to use root, false otherwise.
         * @return the single [CommandResult] instance
         */
        fun execCmd(command: String, isRooted: Boolean): CommandResult {
            return execCmd(arrayOf(command), isRooted, true)
        }

        /**
         * Execute the command.
         *
         * @param commands The commands.
         * @param isRooted True to use root, false otherwise.
         * @return the single [CommandResult] instance
         */
        fun execCmd(commands: List<String>?, isRooted: Boolean): CommandResult {
//            return execCmd(commands?.toArray<String>(arrayOf<String>()), isRooted, true)

            return execCmd(commands?.toTypedArray(), isRooted, true)
        }




        /**
         * Execute the command.
         *
         * @param command         The command.
         * @param isRooted        True to use root, false otherwise.
         * @param isNeedResultMsg True to return the message of result, false otherwise.
         * @return the single [CommandResult] instance
         */
        fun execCmd(
            command: String,
            isRooted: Boolean,
            isNeedResultMsg: Boolean
        ): CommandResult {
            return execCmd(arrayOf(command), isRooted, isNeedResultMsg)
        }

        /**
         * Execute the command.
         *
         * @param commands        The commands.
         * @param isRooted        True to use root, false otherwise.
         * @param isNeedResultMsg True to return the message of result, false otherwise.
         * @return the single [CommandResult] instance
         */
        fun execCmd(
            commands: List<String>?,
            isRooted: Boolean,
            isNeedResultMsg: Boolean
        ): CommandResult {
            return execCmd(
                commands?.toTypedArray(),
                isRooted,
                isNeedResultMsg
            )
        }
        /**
         * Execute the command.
         *
         * @param commands        The commands.
         * @param isRooted        True to use root, false otherwise.
         * @param isNeedResultMsg True to return the message of result, false otherwise.
         * @return the single [CommandResult] instance
         */
        /**
         * Execute the command.
         *
         * @param commands The commands.
         * @param isRooted True to use root, false otherwise.
         * @return the single [CommandResult] instance
         */
        @JvmOverloads
        fun execCmd(
            commands: Array<String>?,
            isRooted: Boolean,
            isNeedResultMsg: Boolean = true
        ): CommandResult {
            var result = -1
            if (commands == null || commands.size == 0) {
                return CommandResult(result, "", "")
            }
            var process: Process? = null
            var successResult: BufferedReader? = null
            var errorResult: BufferedReader? = null
            var successMsg: StringBuilder? = null
            var errorMsg: StringBuilder? = null
            var os: DataOutputStream? = null
            try {
                process = Runtime.getRuntime().exec(if (isRooted) "su" else "sh")
                os = DataOutputStream(process.outputStream)
                for (command in commands) {
                    if (command == null) continue
                    os.write(command.toByteArray())
                    os.writeBytes(LINE_SEP)
                    os.flush()
                }
                os.writeBytes("exit" + LINE_SEP)
                os.flush()
                result = process.waitFor()
                if (isNeedResultMsg) {
                    successMsg = StringBuilder()
                    errorMsg = StringBuilder()
                    successResult = BufferedReader(
                        InputStreamReader(process.inputStream, "UTF-8")
                    )
                    errorResult = BufferedReader(
                        InputStreamReader(process.errorStream, "UTF-8")
                    )
                    var line: String?
                    if (successResult.readLine().also { line = it } != null) {
                        successMsg.append(line)
                        while (successResult.readLine().also { line = it } != null) {
                            successMsg.append(LINE_SEP).append(line)
                        }
                    }
                    if (errorResult.readLine().also { line = it } != null) {
                        errorMsg.append(line)
                        while (errorResult.readLine().also { line = it } != null) {
                            errorMsg.append(LINE_SEP).append(line)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (os != null) {
                        os.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    if (successResult != null) {
                        successResult.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    if (errorResult != null) {
                        errorResult.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                process?.destroy()
            }
            return CommandResult(
                result,
                successMsg?.toString() ?: "",
                errorMsg?.toString() ?: ""
            )
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}