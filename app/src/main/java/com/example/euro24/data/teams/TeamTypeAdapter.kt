package com.example.euro24.data.teams

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class TeamTypeAdapter : TypeAdapter<Team>() {
    override fun write(out: JsonWriter, team: Team) {
        out.beginObject()
        out.name("id").value(team.id)
        out.name("name").value(team.name)
        out.name("qualifiedAs").value(team.qualifiedAs)
        out.name("qualificationDate").value(team.qualificationDate)
        out.name("numberAppearances").value(team.numberAppearances)
        out.name("previousAppearances").value(team.previousAppearances)
        out.name("titles").value(team.titles)
        out.name("bestResult").value(team.bestResult)
        out.name("headCoachId").value(team.headCoachId)
        out.name("headCoach").value(team.headCoach)
        out.name("nicknameOG").value(team.nicknameOG)
        out.name("nicknameEN").value(team.nicknameEN)
        out.name("fifaCode").value(team.fifaCode)
        out.name("KitBrand").value(team.kitBrand)
        out.name("matchesListId")
        out.beginArray()
        for (id in team.matchesListId) {
            out.value(id)
        }
        out.endArray()
        out.name("played").value(team.played)
        out.name("won").value(team.won)
        out.name("drawn").value(team.drawn)
        out.name("lost").value(team.lost)
        out.name("goalsFor").value(team.goalsFor)
        out.name("goalsAgainst").value(team.goalsAgainst)
        out.name("goalDifference").value(team.goalDifference)
        out.name("points").value(team.points)
        out.name("crestUrl").value(team.crestUrl)
        out.name("squadList")
        out.beginArray()
        for (id in team.squadList) {
            out.value(id)
        }
        out.endArray()
        out.endObject()
    }

    override fun read(reader: JsonReader): Team {
        val team = Team()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> team.id = reader.nextInt()
                "name" -> team.name = reader.nextString()
                "qualifiedAs" -> team.qualifiedAs = reader.nextString()
                "qualificationDate" -> team.qualificationDate = reader.nextString()
                "numberAppearances" -> team.numberAppearances = reader.nextInt()
                "previousAppearances" -> team.previousAppearances = reader.nextString()
                "titles" -> team.titles = reader.nextInt()
                "bestResult" -> team.bestResult = reader.nextString()
                "headCoachId" -> team.headCoachId = reader.nextInt()
                "headCoach" -> team.headCoach = reader.nextString()
                "nicknameOG" -> team.nicknameOG = reader.nextString()
                "nicknameEN" -> team.nicknameEN = reader.nextString()
                "fifaCode" -> team.fifaCode = reader.nextString()
                "kitBrand" -> team.kitBrand = reader.nextString()
                "matchesListId" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        team.matchesListId.add(reader.nextInt())
                    }
                    reader.endArray()
                }
                "played" -> team.played = reader.nextInt()
                "won" -> team.won = reader.nextInt()
                "drawn" -> team.drawn = reader.nextInt()
                "lost" -> team.lost = reader.nextInt()
                "goalsFor" -> team.goalsFor = reader.nextInt()
                "goalsAgainst" -> team.goalsAgainst = reader.nextInt()
                "goalDifference" -> team.goalDifference = reader.nextInt()
                "points" -> team.points = reader.nextInt()
                "crestUrl" -> team.crestUrl = reader.nextString()
                "squadList" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        team.squadList.add(reader.nextInt())
                    }
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return team
    }
}