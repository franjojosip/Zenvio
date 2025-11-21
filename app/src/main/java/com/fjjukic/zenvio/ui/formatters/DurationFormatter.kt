package com.fjjukic.zenvio.ui.formatters

import android.content.Context
import com.fjjukic.zenvio.R

/**
 * Formats a single duration:
 * - 45  → "45s"
 * - 75  → "1 min"
 * - 180 → "3 mins"
 */
fun formatDuration(context: Context, seconds: Int): String {
    return if (seconds < 60) {
        context.getString(R.string.unit_seconds, seconds)
    } else {
        val mins = seconds / 60
        if (mins == 1)
            context.getString(R.string.unit_minute, mins)
        else
            context.getString(R.string.unit_minutes, mins)
    }
}

/**
 * Converts to a short numeric value + unit WITHOUT formatting:
 * - 45   → (45, "s")
 * - 120  → (2, "min" or "mins")
 */
fun formatDurationShort(seconds: Int): Pair<Int, String> {
    return if (seconds < 60) {
        seconds to "s"
    } else {
        val mins = seconds / 60
        mins to if (mins == 1) "min" else "mins"
    }
}

/**
 * Handles RANGE formatting:
 *
 * SAME UNIT:
 * - 20–40s
 * - 2–5 mins
 *
 * MIXED UNITS:
 * - 50s–1 min
 */
fun formatDurationRangeCompact(context: Context, start: Int, end: Int): String {
    val (startValue, startUnit) = formatDurationShort(start)
    val (endValue, endUnit) = formatDurationShort(end)

    return if (startUnit == endUnit) {
        // Same unit → show unit only once
        "$startValue–$endValue $endUnit"
    } else {
        // Mixed → show full formatted versions
        "${formatDuration(context, start)}–${formatDuration(context, end)}"
    }
}

/**
 * Final plant formatter:
 * - Single value → formatDuration
 * - Range → formatDurationRangeCompact
 * - Null end → treat as single
 */
fun formatPlantDuration(context: Context, start: Int, end: Int?): String {
    if (end == null || start == end) {
        return formatDuration(context, start)
    }
    return formatDurationRangeCompact(context, start, end)
}