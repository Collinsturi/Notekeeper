package com.rsk.notekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }
    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int{
        val note = NoteInfo(course, noteTitle, noteText)
        notes.add(note)

        return notes.lastIndex
    }
    fun findNote(course: CourseInfo, noteTitle: String, noteText: String) : NoteInfo?{
        for(note in notes){
            if(course == note.course && noteTitle == note.title && noteText == note.text){
                return note
            }
        }
        return null
    }

    private fun initializeCourses() {
        var course = CourseInfo("Android_Intents", "How intents are used as messaging objects")
        courses.set(course.courseId, course);

        course = CourseInfo("android_async", "Keeping the main thread light and performing the heavy tasks in the background")
        courses.set(course.courseId, course)

        course = CourseInfo(courseId = "java_lang", title = "Java Fundamentals: Java Lang")
        courses.set(course.courseId, course)

        course = CourseInfo(title = "The android question", courseId = "Kotlin Vs Java")
        courses.set(course.courseId, course)

    }

    private fun initializeNotes() {
        var note = NoteInfo(courses.get("Android_Intents"), "Android programming with Intents", "I'm learning Android intents")
        notes.add(0, note)

        note = NoteInfo(courses.get("android_async"), "Keeping the main thread light and performing the heavy tasks in the background", "We are keeping the heavy tasks in the background and keeping the UI more responsive")
        notes.add(1, note)

        note = NoteInfo(courses.get("java_lang"),"Java Fundamentals: Java Lang", "I am learning Java fundamentals of the Java language")
        notes.add(2, note)

        note = NoteInfo(courses.get("Android_Intents") , "Android programming with Intents", "I'm learning Android intents")
        notes.add(3, note)

        note = NoteInfo(courses.get("Kotlin Vs Java"),"The android question", "I'm using Java to program android applications")
        notes.add(4, note)

        note = NoteInfo(courses.get("Kotlin Vs Java"),"The android question", "I'm using Kotlin to program android applications")
        notes.add(5, note)
    }
}