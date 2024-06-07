package com.example.euro24.data.matches

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class MatchTypeAdapter : TypeAdapter<Match>() {
    override fun write(out: JsonWriter, match: Match) {
        out.beginObject()
        out.name("id").value(match.id)
        out.name("phase").value(match.phase)
        out.name("dayWeek").value(match.dayWeek)
        out.name("date").value(match.date)
        out.name("time").value(match.time)
        out.name("broadcastPT").value(match.broadcastPT)
        out.name("venueId").value(match.venueId)
        out.name("city").value(match.city)
        out.name("team1").value(match.team1)
        out.name("team1Id").value(match.team1Id)
        out.name("team2").value(match.team2)
        out.name("team2Id").value(match.team2Id)
        out.name("resultTeam1").value(match.resultTeam1)
        out.name("resultTeam2").value(match.resultTeam2)
        out.name("extraTimeTeam1").value(match.extraTimeTeam1)
        out.name("extraTimeTeam2").value(match.extraTimeTeam2)
        out.name("penaltiesTeam1").value(match.penaltiesTeam1)
        out.name("penaltiesTeam2").value(match.penaltiesTeam2)
        out.endObject()
    }

    override fun read(reader: JsonReader): Match {
        val match = Match()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> match.id = reader.nextInt()
                "phase" -> match.phase = reader.nextString()
                "dayWeek" -> match.dayWeek = reader.nextString()
                "date" -> match.date = reader.nextString()
                "time" -> match.time = reader.nextString()
                "broadcastPT" -> match.broadcastPT = reader.nextString()
                "venueId" -> match.venueId = reader.nextInt()
                "city" -> match.city = reader.nextString()
                "team1" -> match.team1 = reader.nextString()
                "team1Id" -> match.team1Id = reader.nextInt()
                "team2" -> match.team2 = reader.nextString()
                "team2Id" -> match.team2Id = reader.nextInt()
                "resultTeam1" -> match.resultTeam1 = reader.nextInt()
                "resultTeam2" -> match.resultTeam2 = reader.nextInt()
                "extraTimeTeam1" -> match.extraTimeTeam1 = reader.nextInt()
                "extraTimeTeam2" -> match.extraTimeTeam2 = reader.nextInt()
                "penaltiesTeam1" -> match.penaltiesTeam1 = reader.nextInt()
                "penaltiesTeam2" -> match.penaltiesTeam2 = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return match
    }
}