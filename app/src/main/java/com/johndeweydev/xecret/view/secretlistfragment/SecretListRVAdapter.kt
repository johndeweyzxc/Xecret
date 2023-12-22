package com.johndeweydev.xecret.view.secretlistfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.johndeweydev.xecret.R
import com.johndeweydev.xecret.model.data.SecretData

class SecretListRVAdapter(private val event: Event):
  RecyclerView.Adapter<SecretListRVAdapter.SecretAdapterViewHolder>() {

  interface Event {
    fun onClickSecret(secretData: SecretData, pos: Int)
  }

  private val secretsList = mutableListOf<SecretData?>()

  class SecretAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardView: MaterialCardView = itemView.findViewById(R.id.materialCardViewSecretItem)
    val name: TextView = itemView.findViewById(R.id.textViewNameSecretItem)
    val description: TextView = itemView.findViewById(R.id.textViewDescriptionSecretItem)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecretAdapterViewHolder {
    val viewHolder = LayoutInflater.from(parent.context).inflate(
      R.layout.rv_secret_item, parent, false)
    return SecretAdapterViewHolder(viewHolder)
  }

  override fun getItemCount(): Int {
    return secretsList.size
  }

  override fun onBindViewHolder(holder: SecretAdapterViewHolder, position: Int) {
    val currentItem = secretsList[position]

    holder.apply {
      cardView.setOnClickListener {
        if (currentItem != null) {
          event.onClickSecret(currentItem.clone(), position)
        }
      }
      name.text = currentItem?.name
      description.text = currentItem?.description
    }
  }

  fun addSecret(secretData: SecretData?) {
    secretsList.add(secretData)
    notifyItemInserted(secretsList.size - 1)
  }

  fun addListOfSecrets(secrets: ArrayList<SecretData>?, filterOutDeletedSecret: Boolean) {
    val currentSize = secretsList.size
    secretsList.clear()
    notifyItemRangeRemoved(0, currentSize)

    if (secrets == null) {
      return
    }

    if (filterOutDeletedSecret) {
      // Remove temporarily deleted secret by only including secrets where the deletedAt attribute
      // is null
      val filteredOutTemporarilyDeletedSecret = secrets.filter { it.deletedAt == null }
      secretsList.addAll(filteredOutTemporarilyDeletedSecret)
      notifyItemRangeInserted(0, secretsList.size)
    } else {
      // Remove non temporarily deleted secret by only including secrets where the deletedAt
      // attribute has a date object
      val filteredOutNonTemporarilyDeletedSecret = secrets.filter { it.deletedAt != null }
      secretsList.addAll(filteredOutNonTemporarilyDeletedSecret)
      notifyItemRangeInserted(0, secretsList.size)
    }
  }

  fun removeSecret(position: Int): SecretData? {
    val secretData = secretsList.removeAt(position)
    notifyItemRemoved(position)
    return secretData
  }

  fun insertSecret(position: Int, secretData: SecretData) {
    secretsList.add(position, secretData)
    notifyItemInserted(position)
  }
}