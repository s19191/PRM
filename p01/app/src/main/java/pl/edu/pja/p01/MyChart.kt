package pl.edu.pja.p01

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.math.abs

class MyChart(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val calendar = Calendar.getInstance()

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        textSize = 20f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        with(canvas) {
            val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
            var xTmp = ((width.toFloat() - 50f) / daysInMonth) + 50f
            val startBalance = calculateStartBalance()
            val maximumBalance = calculateMaximumBalance()
            println("Max$maximumBalance")
            val minimumBalance = calculateMinimumBalance()
            println("Min$minimumBalance")
            val minimumBalanceAbs = abs(minimumBalance)
            drawLine(50f, 0f, 50f, height.toFloat(), paint)
            when {
                maximumBalance <= 0 -> {
                    drawLine(50f, 5f, width.toFloat(), 5f, paint)
                    for (i in 1..daysInMonth) {
                        drawText(i.toString().plus("\t"), xTmp, 30f, paint)
                        xTmp += (width.toFloat() - 50f) / daysInMonth
                    }
                    var yTmp = 15f
                    when {
                        minimumBalanceAbs >= 100000 -> {
                            val rounded: Int = (minimumBalanceAbs.toInt() + 9999) / 10000 * 10000
                            val howManyParts = rounded / 10000
                            for (i in 0..rounded step 10000) {
                                drawText("-$i", 0f, yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        minimumBalanceAbs in 10000.0..100000.0 -> {
                            val rounded: Int = (minimumBalanceAbs.toInt() + 999) / 1000 * 1000
                            val howManyParts = rounded / 1000
                            for (i in 0..rounded step 1000) {
                                drawText("-$i", 0f, yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        minimumBalanceAbs in 1000.0..10000.0 -> {
                            val rounded: Int = (minimumBalanceAbs.toInt() + 99) / 100 * 100
                            val howManyParts = rounded / 100
                            for (i in 0..rounded step 100) {
                                drawText("-$i", 0f, yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        minimumBalanceAbs in 100.0..1000.0 -> {
                            val rounded: Int = (minimumBalanceAbs.toInt() + 9) / 10 * 10
                            val howManyParts = rounded / 10
                            for (i in 0..rounded step 10) {
                                drawText("-$i", 0f, yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        minimumBalanceAbs in 0.0..100.0 -> {
                            val howManyParts: Int = minimumBalanceAbs.toInt() / 10
                            for (i in 0..minimumBalanceAbs.toInt() step 1) {
                                drawText("-$i", 0f, yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                    }
                }
                minimumBalance >= 0 -> {
                    drawLine(50f, height.toFloat() - 5f, width.toFloat(), height.toFloat() - 5f, paint)
                    for (i in 1..daysInMonth) {
                        drawText(i.toString().plus("\t"), xTmp, height.toFloat() - 30f, paint)
                        xTmp += (width.toFloat() - 50f) / daysInMonth
                    }
                    var yTmp = 5f
                    when {
                        maximumBalance >= 100000 -> {
                            val rounded: Int = (maximumBalance.toInt() + 9999) / 10000 * 10000
                            val howManyParts = rounded / 10000
                            for (i in 0..rounded step 10000) {
                                drawText(i.toString(), 0f, height.toFloat() - yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        maximumBalance in 10000.0..100000.0 -> {
                            val rounded: Int = (maximumBalance.toInt() + 999) / 1000 * 1000
                            val howManyParts = rounded / 1000
                            for (i in 0..rounded step 1000) {
                                drawText(i.toString(), 0f, height.toFloat() - yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        maximumBalance in 1000.0..10000.0 -> {
                            val rounded: Int = (maximumBalance.toInt() + 99) / 100 * 100
                            val howManyParts = rounded / 100
                            for (i in 0..rounded step 100) {
                                drawText(i.toString(), 0f, height.toFloat() - yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        maximumBalance in 100.0..1000.0 -> {
                            val rounded: Int = (maximumBalance.toInt() + 9) / 10 * 10
                            val howManyParts = rounded / 10
                            for (i in 0..rounded step 10) {
                                drawText(i.toString(), 0f, height.toFloat() - yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                        maximumBalance in 0.0..100.0 -> {
                            val howManyParts: Int = maximumBalance.toInt() / 10
                            for (i in 0..maximumBalance.toInt() step 1) {
                                drawText(i.toString(), 0f, height.toFloat() - yTmp, paint)
                                yTmp += (height.toFloat() - 20f) / howManyParts
                            }
                        }
                    }
                }
                else -> {
                    var roundedMaximumBalance = 0
                    when {
                        maximumBalance >= 100000 -> {
                            roundedMaximumBalance = (maximumBalance.toInt() + 9999) / 10000 * 10000
                        }
                        maximumBalance in 10000.0..100000.0 -> {
                            roundedMaximumBalance = (maximumBalance.toInt() + 999) / 1000 * 1000
                        }
                        maximumBalance in 1000.0..10000.0 -> {
                            roundedMaximumBalance = (maximumBalance.toInt() + 99) / 100 * 100
                        }
                        maximumBalance in 100.0..1000.0 -> {
                            roundedMaximumBalance = (maximumBalance.toInt() + 9) / 10 * 10
                        }
                        maximumBalance in 0.0..100.0 -> {
                            roundedMaximumBalance = maximumBalance.toInt()
                        }
                    }
                    var roundedMinimumBalance = 0
                    when {
                        minimumBalanceAbs >= 100000 -> {
                            roundedMinimumBalance = (minimumBalanceAbs.toInt() + 9999) / 10000 * 10000
                        }
                        minimumBalanceAbs in 10000.0..100000.0 -> {
                            roundedMinimumBalance = (minimumBalanceAbs.toInt() + 999) / 1000 * 1000
                        }
                        minimumBalanceAbs in 1000.0..10000.0 -> {
                            roundedMinimumBalance = (minimumBalanceAbs.toInt() + 99) / 100 * 100
                        }
                        minimumBalanceAbs in 100.0..1000.0 -> {
                            roundedMinimumBalance = (minimumBalanceAbs.toInt() + 9) / 10 * 10
                        }
                        minimumBalanceAbs in 0.0..100.0 -> {
                            roundedMinimumBalance = minimumBalanceAbs.toInt()
                        }
                    }
                    var sumOfRoundedBalances = roundedMaximumBalance + roundedMinimumBalance
                    var roundedSumOfRoundedBalances = 0
                    var howManyParts = 0
                    val sumBalanceAbs = maximumBalance + minimumBalanceAbs
//                        if (maximumBalance <= minimumBalanceAbs) {
                        drawLine(
                            50f,
                            height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat(),
                            width.toFloat(),
                            height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat(),
                            paint
                        )
                        for (i in 1..daysInMonth) {
                            drawText(
                                i.toString().plus("\t"),
                                    xTmp,
                                    height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + 30f,
                                    paint
                            )
                            xTmp += (width.toFloat() - 50f) / daysInMonth
                        }
                            var yTmp = -5f
                            when {
                                sumOfRoundedBalances >= 100000 -> {
                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 9999) / 10000 * 10000
                                    howManyParts = roundedSumOfRoundedBalances / 10000
                                    for (i in 0..roundedMaximumBalance step 10000) {
                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                    yTmp = 5f
                                    for (i in 0..roundedMinimumBalance step 10000) {
                                        if (i != 0) {
                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
                                        }
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                }
                                sumOfRoundedBalances in 10000..100000 -> {
                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 999) / 1000 * 1000
                                    howManyParts = roundedSumOfRoundedBalances / 1000
                                    for (i in 0..roundedMaximumBalance step 1000) {
                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                    yTmp = 5f
                                    for (i in 0..roundedMinimumBalance step 1000) {
                                        if (i != 0) {
                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
                                        }
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                }
                                sumOfRoundedBalances in 1000..10000 -> {
                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 99) / 100 * 100
                                    howManyParts = roundedSumOfRoundedBalances / 100
                                    for (i in 0..roundedMaximumBalance step 100) {
                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                    yTmp = 5f
                                    for (i in 0..roundedMinimumBalance step 100) {
                                        if (i != 0) {
                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
                                        }
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                }
                                sumOfRoundedBalances in 100..1000 -> {
                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 9) / 10 * 10
                                    howManyParts = roundedSumOfRoundedBalances / 10
                                    for (i in 0..roundedMaximumBalance step 10) {
                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                    yTmp = 5f
                                    for (i in 0..roundedMinimumBalance step 10) {
                                        if (i != 0) {
                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
                                        }
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                }
                                sumOfRoundedBalances in 0..100 -> {
                                    howManyParts = sumOfRoundedBalances
                                    for (i in 0..roundedMaximumBalance step 1) {
                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                    yTmp = 5f
                                    for (i in 0..roundedMinimumBalance step 1) {
                                        if (i != 0) {
                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
                                        }
                                        yTmp += (height.toFloat() - 50f) / howManyParts
                                    }
                                }
                            }
//                    } else {
//                        drawLine(
//                            50f,
//                            height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat(),
//                            width.toFloat(),
//                            height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat(),
//                            paint
//                        )
//                        for (i in 1..daysInMonth) {
//                            drawText(
//                                i.toString().plus("\t"),
//                                xTmp,
//                                height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + 30f,
//                                paint
//                            )
//                            xTmp += (width.toFloat() - 50f) / daysInMonth
//                        }
//                            var yTmp = -5f
//                            when {
//                                sumOfRoundedBalances >= 100000 -> {
//                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 9999) / 10000 * 10000
//                                    howManyParts = roundedSumOfRoundedBalances / 10000
//                                    for (i in 0..roundedMaximumBalance step 10000) {
//                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                    yTmp = 5f
//                                    for (i in 0..roundedMinimumBalance step 10000) {
//                                        if (i != 0) {
//                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
//                                        }
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                }
//                                sumOfRoundedBalances in 10000..100000 -> {
//                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 999) / 1000 * 1000
//                                    howManyParts = roundedSumOfRoundedBalances / 1000
//                                    for (i in 0..roundedMaximumBalance step 1000) {
//                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                    yTmp = 5f
//                                    for (i in 0..roundedMinimumBalance step 1000) {
//                                        if (i != 0) {
//                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
//                                        }
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                }
//                                sumOfRoundedBalances in 1000..10000 -> {
//                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 99) / 100 * 100
//                                    howManyParts = roundedSumOfRoundedBalances / 100
//                                    for (i in 0..roundedMaximumBalance step 100) {
//                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                    yTmp = 5f
//                                    for (i in 0..roundedMinimumBalance step 100) {
//                                        if (i != 0) {
//                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
//                                        }
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                }
//                                sumOfRoundedBalances in 100..1000 -> {
//                                    roundedSumOfRoundedBalances = (sumOfRoundedBalances + 9) / 10 * 10
//                                    howManyParts = roundedSumOfRoundedBalances / 10
//                                    for (i in 0..roundedMaximumBalance step 10) {
//                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                    yTmp = 5f
//                                    for (i in 0..roundedMinimumBalance step 10) {
//                                        if (i != 0) {
//                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
//                                        }
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                }
//                                sumOfRoundedBalances in 0..100 -> {
//                                    howManyParts = sumOfRoundedBalances
//                                    for (i in 0..roundedMaximumBalance step 1) {
//                                        drawText(i.toString(), 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() - yTmp, paint)
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                    yTmp = 5f
//                                    for (i in 0..roundedMinimumBalance step 1) {
//                                        if (i != 0) {
//                                            drawText("-$i", 0f, height.toFloat() * (maximumBalance / sumBalanceAbs).toFloat() + yTmp, paint)
//                                        }
//                                        yTmp += (height.toFloat() - 50f) / howManyParts
//                                    }
//                                }
//                            }
//                    }
                }
            }
        }
    }

    private fun calculateStartBalance() : Double {
        var sum = 0.0
        for (i in Shared.expenseList) {
            calendar.timeInMillis = i.date
            val fromWhen = Calendar.getInstance()
            fromWhen.set(Calendar.DAY_OF_MONTH, 1)
            fromWhen.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) - 1)
            if (calendar.before(fromWhen)) {
                sum += i.amount
            }
        }
        return sum
    }

    private fun calculateMaximumBalance() : Double {
        var sum = calculateStartBalance()
        var maximumBalance = sum
        for (i in Shared.expenseList) {
            calendar.timeInMillis = i.date
            val fromWhen = Calendar.getInstance()
            fromWhen.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) - 2)
            fromWhen.set(Calendar.DAY_OF_MONTH, fromWhen.getActualMaximum(Calendar.DAY_OF_MONTH))
            val toWhen = Calendar.getInstance()
            toWhen.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
            toWhen.set(Calendar.DAY_OF_MONTH, 1)
            if (calendar.after(fromWhen) && calendar.before(toWhen)) {
                sum += i.amount
            }
            if (sum >= maximumBalance) {
                maximumBalance = sum
            }
        }
        return maximumBalance
    }

    private fun calculateMinimumBalance() : Double {
        var sum = calculateStartBalance()
        var minimumBalance = sum
        for (i in Shared.expenseList) {
            calendar.timeInMillis = i.date
            val fromWhen = Calendar.getInstance()
            fromWhen.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) - 2)
            fromWhen.set(Calendar.DAY_OF_MONTH, fromWhen.getActualMaximum(Calendar.DAY_OF_MONTH))
            val toWhen = Calendar.getInstance()
            toWhen.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
            toWhen.set(Calendar.DAY_OF_MONTH, 1)
            if (calendar.after(fromWhen) && calendar.before(toWhen)) {
                sum += i.amount
            }
            if (sum <= minimumBalance) {
                minimumBalance = sum
            }
        }
        return minimumBalance
    }
}