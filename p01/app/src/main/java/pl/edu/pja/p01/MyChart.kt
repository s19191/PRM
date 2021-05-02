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
            var xTmp = width.toFloat() / daysInMonth
            val startBalance = calculateStartBalance()
            val maximumBalance = calculateMaximumBalance()
            println("Max$maximumBalance")
            val minimumBalance = calculateMinimumBalance()
            println("Min$minimumBalance")
            when {
                maximumBalance <= 0 -> {
                    drawLine(0f, 5f, width.toFloat(), 5f, paint)
                    for (i in 1..daysInMonth) {
                        drawText(i.toString().plus("\t"), xTmp, 30f, paint)
                        xTmp += width.toFloat() / daysInMonth
                    }
                }
                minimumBalance >= 0 -> {
                    drawLine(0f, height.toFloat() - 5f, width.toFloat(), height.toFloat() - 5f, paint)
                    for (i in 1..daysInMonth) {
                        drawText(i.toString().plus("\t"), xTmp, height.toFloat() - 30f, paint)
                        xTmp += width.toFloat() / daysInMonth
                    }
                }
                else -> {
                    if (maximumBalance >= abs(minimumBalance)) {
                        drawLine(
                            0f,
                            height.toFloat() * (abs(minimumBalance)/(maximumBalance + abs(minimumBalance))).toFloat(),
                            width.toFloat(),
                            height.toFloat() * (abs(minimumBalance)/(maximumBalance + abs(minimumBalance))).toFloat(),
                            paint)
                        for (i in 1..daysInMonth) {
                            drawText(i.toString().plus("\t"), xTmp, height.toFloat() * (abs(minimumBalance)/(maximumBalance + abs(minimumBalance))).toFloat() + 30f, paint)
                            xTmp += width.toFloat() / daysInMonth
                        }
                    } else {
                        drawLine(
                            0f,
                            height.toFloat() * (maximumBalance/(abs(minimumBalance) + maximumBalance)).toFloat(),
                            width.toFloat(),
                            height.toFloat() * (maximumBalance/(abs(minimumBalance) + maximumBalance)).toFloat(),
                            paint)
                        for (i in 1..daysInMonth) {
                            drawText(i.toString().plus("\t"), xTmp, height.toFloat() * (maximumBalance/(abs(minimumBalance) + maximumBalance)).toFloat() + 30f, paint)
                            xTmp += width.toFloat() / daysInMonth
                        }
                    }
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
        return sum
    }
}