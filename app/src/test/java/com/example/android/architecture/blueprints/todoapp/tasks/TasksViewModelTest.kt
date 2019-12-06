package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: FakeTasksRepository

    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setup() {
        tasksRepository = FakeTasksRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)
        // GIVEN a fresh TasksViewModel
        tasksViewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // WHEN adding a new task
        tasksViewModel.addNewTask()

        // THEN the new task event is triggered
        val result = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(result.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        val result = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(result, `is`(true))
    }
}