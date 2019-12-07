package com.example.android.architecture.blueprints.todoapp.tasks

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TasksFragmentTest {

    private lateinit var repository: TasksRepository

    @Before
    fun setUp() {
        repository = FakeAndroidTasksRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
        // GIVEN - On the home screen with 2 tasks saved in repository
        repository.saveTask(Task("title1", "description1", false, "id1"))
        repository.saveTask(Task("title2", "description2", true, "id2"))

        val scenario =
                launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.tasks_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("title1")), click()
                ))

        // THEN - Verify that we navigate to the first detail screen
        verify(navController)
                .navigate(TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1"))
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() {
        // GIVEN - On the home screen
        val scenario =
                launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on add task button
        onView(withId(R.id.add_task_fab))
                .perform(click())

        // THEN - Verify that we navigate to the add/edit task screen
        verify(navController)
                .navigate(TasksFragmentDirections
                        .actionTasksFragmentToAddEditTaskFragment(
                                null,
                                getApplicationContext<Context>().getString(R.string.add_task)
                        )
                )
    }
}