package com.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import com.steps.util.TestAdapter
import com.steps.util.TestStepFragment
import junit.framework.Assert.assertEquals
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class StepBarTest {
    private lateinit var viewPager: androidx.viewpager.widget.ViewPager

    private lateinit var stepBar: StepBar

    private lateinit var nextStep: Button
    private lateinit var backStep: Button
    private lateinit var doneButton: Button

    @Before
    fun setUp() {
        stepBar = inflateStepBar()
        viewPager = androidx.viewpager.widget.ViewPager(RuntimeEnvironment.application.baseContext)

        nextStep = stepBar.findViewById(R.id.nextStep)
        backStep = stepBar.findViewById(R.id.backStep)
        doneButton = stepBar.findViewById(R.id.doneStep)
    }

    @Test
    @Ignore
    fun disabledDefaultStateForNextStepButton() {
        assertThat(nextStep).isDisabled
    }

    @Test
    @Ignore
    fun disabledDefaultStateForBackStepButton() {
        assertThat(backStep).isDisabled
    }


    @Test
    @Ignore
    fun invisibleDefaultStateForDoneButton() {
        assertThat(doneButton).isInvisible
    }

    @Test
    @Ignore
    fun enableNextButtonWhenStepIsValid() {
        viewPager.adapter = getAdapter(step(isValid = true), step(isValid = false))
        stepBar.setViewPager(viewPager)

        assertThat(nextStep).isEnabled
    }

    @Test
    @Ignore
    fun turnDoneButtonVisibleOnLastStep() {
        viewPager.adapter = getAdapter(step(isValid = true), step(isValid = false))
        stepBar.setViewPager(viewPager)
        nextStep.performClick()

        assertThat(nextStep).isInvisible
        assertThat(doneButton).isVisible
    }

    @Test
    @Ignore
    fun callsOnCompleteWithAllStepValuesOnPressDone() {
        val bundle = Bundle()
        val step1Value = "step 1 value"
        val step2Value = "step 2 value"

        viewPager.adapter = getAdapter(validStep("step1", step1Value), validStep("step2", step2Value))
        stepBar.setViewPager(viewPager)

        stepBar.setOnCompleteListener { bundle.putAll(it) }

        nextStep.performClick()
        doneButton.performClick()


        assertEquals(step1Value, bundle.getString("step1"))
        assertEquals(step2Value, bundle.getString("step2"))

    }

    private fun inflateStepBar(): StepBar {
        return LayoutInflater
                .from(RuntimeEnvironment.application)
                .inflate(R.layout.step_bar,
                        StepBar(RuntimeEnvironment.application, Robolectric.buildAttributeSet().build()),
                        true) as StepBar
    }

    private fun getAdapter(vararg steps: Step): StepAdapter {
        return TestAdapter(steps.toList(), fragmentManager())
    }

    private fun step(isValid: Boolean): Step {
        val step = TestStepFragment()
        val bundle = Bundle()

        bundle.putBoolean("isValid", isValid)

        step.arguments = bundle
        return step
    }

    private fun validStep(key: String, result: String): Step {
        val step = TestStepFragment()
        val bundle = Bundle()

        bundle.putBoolean("isValid", true)
        bundle.putString("key", key)
        bundle.putString("result", result)

        step.arguments = bundle
        return step
    }

    private fun fragmentManager(): androidx.fragment.app.FragmentManager {
        return Robolectric
                .buildActivity(androidx.fragment.app.FragmentActivity::class.java)
                .create()
                .get().supportFragmentManager
    }
}

