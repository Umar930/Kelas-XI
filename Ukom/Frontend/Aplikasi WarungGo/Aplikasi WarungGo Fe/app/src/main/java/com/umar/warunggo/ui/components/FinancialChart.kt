package com.umar.warunggo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.umar.warunggo.data.model.ChartDataPoint
import com.umar.warunggo.data.model.ChartType

/**
 * Financial Chart Component
 * Displays revenue, expense, or profit data
 */
@Composable
fun FinancialChart(
    data: List<ChartDataPoint>,
    selectedType: ChartType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Grafik Keuangan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Chart Canvas
            if (data.isNotEmpty()) {
                ChartCanvas(
                    data = data,
                    selectedType = selectedType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

/**
 * Chart Canvas - Simple line chart implementation
 */
@Composable
private fun ChartCanvas(
    data: List<ChartDataPoint>,
    selectedType: ChartType,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        
        // Get values based on selected type
        val values = data.map {
            when (selectedType) {
                ChartType.REVENUE -> it.revenue
                ChartType.EXPENSE -> it.expense
                ChartType.PROFIT -> it.profit
            }
        }
        
        if (values.isEmpty()) return@Canvas
        
        val maxValue = values.maxOrNull() ?: 0.0
        val minValue = values.minOrNull() ?: 0.0
        val range = maxValue - minValue
        
        if (range == 0.0) return@Canvas
        
        // Calculate points
        val xStep = (width - padding * 2) / (values.size - 1).coerceAtLeast(1)
        
        val points = values.mapIndexed { index, value ->
            val x = padding + index * xStep
            val normalizedValue = ((value - minValue) / range).toFloat()
            val y = height - padding - (normalizedValue * (height - padding * 2))
            Offset(x, y)
        }
        
        // Draw grid lines
        for (i in 0..4) {
            val y = padding + i * (height - padding * 2) / 4
            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )
        }
        
        // Draw line chart
        val path = Path()
        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
        }
        
        val chartColor = when (selectedType) {
            ChartType.REVENUE -> primaryColor
            ChartType.EXPENSE -> errorColor
            ChartType.PROFIT -> tertiaryColor
        }
        
        // Draw line
        drawPath(
            path = path,
            color = chartColor,
            style = Stroke(width = 4f)
        )
        
        // Draw points
        points.forEach { point ->
            drawCircle(
                color = chartColor,
                radius = 6f,
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3f,
                center = point
            )
        }
    }
    
    // X-axis labels
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.forEach { point ->
            Text(
                text = point.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
