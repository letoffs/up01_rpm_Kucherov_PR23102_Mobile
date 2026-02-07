package com.example.collegeschedule.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collegeschedule.data.dto.LessonDto
import com.example.collegeschedule.data.dto.LessonGroupPart
import com.example.collegeschedule.data.dto.LessonPartDto

@Composable
fun LessonCard(
    lesson: LessonDto,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = getBuildingColor(lesson.building)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Заголовок с номером пары и временем
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Время",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Пара ${lesson.lessonNumber}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = lesson.time,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Основная информация
            LessonInfoRow(
                icon = Icons.Default.Book,
                text = lesson.subject
            )

            LessonInfoRow(
                icon = Icons.Default.Person,
                text = lesson.teacher
            )

            LessonInfoRow(
                icon = Icons.Default.LocationOn,
                text = "${lesson.building}, ауд. ${lesson.classroom}"
            )

            // Подгруппы
            val subgroups = lesson.groupParts.filter { it.value != null }
            if (subgroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Подгруппы:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                // Используем Column для отображения подгрупп
                Column {
                    subgroups.forEach { (part, info) ->
                        info?.let {
                            SubgroupInfo(part = part, info = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonInfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SubgroupInfo(
    part: LessonGroupPart,
    info: LessonPartDto
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = when (part) {
                    LessonGroupPart.FULL -> "Вся группа"
                    LessonGroupPart.SUB1 -> "1 подгруппа"
                    LessonGroupPart.SUB2 -> "2 подгруппа"
                },
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Предмет: ${info.subject}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Преподаватель: ${info.teacher}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Аудитория: ${info.building}, ${info.classroom}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun getBuildingColor(building: String): Color {
    return when {
        building.contains("Главный", ignoreCase = true) -> Color(0xFFE3F2FD) // Светло-голубой
        building.contains("Учебный", ignoreCase = true) -> Color(0xFFE8F5E9) // Светло-зеленый
        building.contains("Лабораторный", ignoreCase = true) -> Color(0xFFF3E5F5) // Светло-фиолетовый
        building.contains("Спортивный", ignoreCase = true) -> Color(0xFFFFF3E0) // Светло-оранжевый
        else -> MaterialTheme.colorScheme.surface
    }
}
