package org.wordpress.android.ui.screenshots;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.CardView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import junit.framework.AssertionFailedError;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import org.junit.ClassRule;
import org.wordpress.android.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wordpress.android.datasets.ReaderTagTable;
import org.wordpress.android.models.ReaderTag;
import org.wordpress.android.models.ReaderTagList;
import org.wordpress.android.ui.WPLaunchActivity;
import org.wordpress.android.ui.reader.views.ReaderSiteHeaderView;
import org.wordpress.android.util.image.ImageType;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;

import static junit.framework.Assert.fail;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.wordpress.android.BuildConfig.SCREENSHOT_LOGINPASSWORD;
import static org.wordpress.android.BuildConfig.SCREENSHOT_LOGINUSERNAME;

import static org.wordpress.android.ui.screenshots.support.WPScreenshotSupport.*;
@LargeTest
@RunWith(AndroidJUnit4.class)
public class WPScreenshotTest {
    private static final int ATTEMPTS = 50;
    private static final int WAITING_TIME = 300;

    @ClassRule
    public static final WPLocaleTestRule LOCALE_TEST_RULE = new WPLocaleTestRule();


    @Rule
    public ActivityTestRule<WPLaunchActivity> mActivityTestRule = new ActivityTestRule<>(WPLaunchActivity.class,
            false, false);

    @Test
    public void wPScreenshotTest() {
        mActivityTestRule.launchActivity(null);
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());

        wPLogin();
        navigateReader();
        editBlogPost();
        navigateNotifications();
        navigateStats();
        wPLogout();
    }

    private void wPLogin() {
        // If we're already logged in, log out before starting
        if (!hasElement(R.id.login_button)) {
            wPLogout();
        }

        // Login Prologue – We want to log in, not sign up
        // See LoginPrologueFragment
        clickOn(R.id.login_button);

        // Email Address Screen – Fill it in and click "Next"
        // See LoginEmailFragment
        populateTextField(R.id.input, SCREENSHOT_LOGINUSERNAME);
        clickOn(R.id.primary_button);

        // Receive Magic Link or Enter Password Screen – Choose "Enter Password"
        // See LoginMagicLinkRequestFragment
        clickOn(R.id.login_enter_password);

        // Password Screen – Fill it in and click "Next"
        // See LoginEmailPasswordFragment
        populateTextField(R.id.input, SCREENSHOT_LOGINPASSWORD);
        clickOn(R.id.primary_button);

        // Login Confirmation Screen – Click "Continue"
        // See LoginEpilogueFragment
        clickOn(R.id.primary_button);
    }

    private void wPLogout() {
        // Click on the "Me" tab in the nav, then choose "Log Out"
        clickOn(R.id.nav_me);
        scrollToThenClickOn(R.id.row_logout);

        // Confirm that we want to log out
        clickOn(android.R.id.button1);
    }

    private void navigateReader() {
        // Choose the "Reader" tab in the nav
        clickOn(R.id.nav_reader);

        // Choose "Discover" from the spinner, but first, choose another item
        // to force a re-load – this avoids locale issues
        selectItemAtIndexInSpinner(getDiscoverTagIndex() == 0 ? 1 : 0, R.id.filter_spinner);
        selectItemAtIndexInSpinner(getDiscoverTagIndex(), R.id.filter_spinner);

        // Wait for the blog articles to load
        waitForAtLeastOneElementOfTypeToExist(ReaderSiteHeaderView.class);
        waitForAtLeastOneElementOfTypeToExist(CardView.class);
        waitForImagesOfTypeWithPlaceholder(R.id.image_featured, ImageType.PHOTO);
        waitForImagesOfTypeWithPlaceholder(R.id.image_avatar, ImageType.AVATAR);
        waitForImagesOfTypeWithPlaceholder(R.id.image_blavatar, ImageType.BLAVATAR);

        waitForRecyclerViewToStopReloading();

        Screengrab.screenshot("screenshot_2");
    }

    private void editBlogPost() {
        // Blog button on Nav Bar
        ViewInteraction blogNavBar = onView(
                allOf(withId(R.id.nav_sites), childAtPosition(
                        childAtPosition(withId(R.id.bottom_navigation), 0), 0)));
        waitForElementUntilDisplayed(blogNavBar).perform(click());
        Screengrab.screenshot("screenshot_3");

        // Blog posts button
        ViewInteraction blogPostsButton = onView(
                allOf(withId(R.id.row_blog_posts), childAtPosition(
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 6), 0)));
        waitForElementUntilDisplayed(blogPostsButton).perform(scrollTo(), click());

        // Waiting for the blog articles to load and edit the first post
        ViewInteraction postCard = onView(
                allOf(withId(R.id.card_view), childAtPosition(
                        withId(R.id.recycler_view), 0)));
        waitForElementUntilDisplayed(postCard).perform(click());

        ViewInteraction aztecText = onView(
                allOf(withId(R.id.aztec), childAtPosition(
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 2), 0)));
        waitForElementUntilDisplayed(aztecText);

        ViewInteraction postTitle = onView(
                allOf(withId(R.id.title), childAtPosition(
                        withClassName(is("android.widget.RelativeLayout")), 0)));
        postTitle.perform(scrollTo(), click());

        // Wait for images in post to load
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Screengrab.screenshot("screenshot_1");

        // Exit
        ViewInteraction navigateUpButton = onView(allOf(childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container), 0)), 1)));
        waitForElementUntilDisplayed(navigateUpButton).perform(click());

        navigateUpButton = onView(allOf(childAtPosition(
                allOf(withId(R.id.toolbar),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")), 0)), 2)));
        waitForElementUntilDisplayed(navigateUpButton).perform(click());
    }

    private void navigateNotifications() {
        // Notification button
        ViewInteraction notificationButton = onView(
                allOf(withId(R.id.nav_notifications), childAtPosition(
                        childAtPosition(withId(R.id.bottom_navigation), 0), 4)));
        waitForElementUntilDisplayed(notificationButton).perform(click());
        Screengrab.screenshot("screenshot_5");
    }

    private void navigateStats() {
        // Stats button
        ViewInteraction statsButton = onView(
                allOf(withId(R.id.nav_sites), childAtPosition(
                        childAtPosition(withId(R.id.bottom_navigation), 0), 0)));
        waitForElementUntilDisplayed(statsButton).perform(click());

        ViewInteraction linearLayout2 = onView(allOf(withId(R.id.row_stats), childAtPosition(
                childAtPosition(withId(R.id.scroll_view), 0), 1)));
        waitForElementUntilDisplayed(linearLayout2).perform(scrollTo(), click());

        // Close the dialog
        ViewInteraction dialogButton = onView(allOf(withId(R.id.promo_dialog_button_positive),
                        childAtPosition(childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")), 3), 1)));
        try {
            // It may open or not, so catch the error if it's not up
            waitForElementUntilDisplayed(dialogButton).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Select Days view
        ViewInteraction spinner = onView(
                allOf(withId(R.id.filter_spinner), childAtPosition(
                        withId(R.id.toolbar_filter), 0)));
        waitForElementUntilDisplayed(spinner).perform(click());

        ViewInteraction spinnerItem = onView(
                allOf(withId(R.id.text), childAtPosition(
                        withClassName(is("android.widget.DropDownListView")), 1)));
        waitForElementUntilDisplayed(spinnerItem).perform(click());

        // Wait a bit
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Screengrab.screenshot("screenshot_4");

        // Navigate up
        ViewInteraction navUpButton = onView(allOf(childAtPosition(allOf(withId(R.id.toolbar),
                childAtPosition(withClassName(is("android.widget.LinearLayout")), 0)), 2)));
        waitForElementUntilDisplayed(navUpButton).perform(click());
    }

    private static ViewInteraction waitForElementUntilDisplayed(ViewInteraction element) {
        int i = 0;
        while (i++ < ATTEMPTS) {
            try {
                element.check(matches(isDisplayed()));
                return element;
            } catch (Exception | AssertionFailedError e) {
                e.printStackTrace();
                try {
                    Thread.sleep(WAITING_TIME);
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static int getDiscoverTagIndex() {
        ReaderTagList tagList = ReaderTagTable.getDefaultTags();
        for (int i = 0; i < tagList.size(); i++) {
            ReaderTag tag = tagList.get(i);
            if (tag.isDiscover()) {
                return i;
            }
        }
        return -1;
    }
}
