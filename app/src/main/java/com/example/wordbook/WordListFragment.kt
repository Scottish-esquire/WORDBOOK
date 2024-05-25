package com.example.wordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class WordListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordAdapter
    private lateinit var roomHelper: RoomHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_list, container, false)
        roomHelper = RoomHelper(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = WordAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val topicId = arguments?.getInt("TOPIC_NAME") ?: -1
        if (topicId != -1) {
            lifecycleScope.launch {
                val topic = roomHelper.topicDao.getTopicNameById(topicId)
                val topicName = topic?.name ?: "Название темы"
                view.findViewById<TextView>(R.id.topicTitle).text = topicName

                val wordCount = roomHelper.wordDao.getWordCountForTopic(topicId)
                println(wordCount)
                if (wordCount > 0) {
                    val words = roomHelper.wordDao.getWordsForTopic(topicId)
                    println(words)
                    adapter.updateWords(words)
                    recyclerView.visibility = View.VISIBLE
                } else {
                    view.findViewById<TextView>(R.id.noWordsText).visibility = View.VISIBLE
                }
            }
        }
    }
}
